package com.group11.common.config;

import com.group11.common.limit.AccessLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 访问拦截器配置
 *
 * @author Xu Haitong
 * @since 2021/11/10 21:36
 */
@Configuration
public class IntercepterConfig implements WebMvcConfigurer {
    @Autowired
    private AccessLimitInterceptor accessLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLimitInterceptor).addPathPatterns("/**");
    }
}
