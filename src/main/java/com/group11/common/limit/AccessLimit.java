package com.group11.common.limit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限制单个 ip 一定时间内的访问次数
 *
 * @author Xu Haitong
 * @since 2021/11/10 21:22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {
    // 最大访问次数
    int maxCount() default 5;

    // 一定时间
    int seconds() default 1;
}
