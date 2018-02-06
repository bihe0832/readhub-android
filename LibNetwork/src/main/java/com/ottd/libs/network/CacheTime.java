package com.ottd.libs.network;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by hardyshi on 2017/9/30.
 */

@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface CacheTime {

    String HEADER_KEY_NAME = "cacheTime";
    // 根据拦截器中的设置动态设置缓存时间
    int CACHE_DURATION_DEFAULT = -1;
    // 不使用缓存
    int CACHE_DURATION_NONE = 0;
    int value() default -1;
}
