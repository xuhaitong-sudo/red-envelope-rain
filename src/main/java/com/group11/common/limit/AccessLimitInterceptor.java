package com.group11.common.limit;

import com.alibaba.fastjson.JSON;
import com.group11.common.exception.ErrorCodeEume;
import com.group11.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author Xu Haitong
 * @since 2021/11/10 21:24
 */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();

            // 放在 Redis 中的 key
            String key = request.getRemoteAddr() + ":" + request.getContextPath() + ":" + request.getServletPath();
            Long count = redisTemplate.opsForValue().increment(key, 1);
            if (count == 1) {
                redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
                return true;
            }
            if (count > maxCount) {  // 进行拒绝
                R r = R.error(ErrorCodeEume.ACCESS_LIMIT).put("data", null);
                OutputStream out = response.getOutputStream();
                out.write(JSON.toJSONString(r).getBytes("UTF-8"));
                out.flush();
                out.close();
                return false;
            }
        }
        return true;
    }

}
