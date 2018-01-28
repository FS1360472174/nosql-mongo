package com.fs.mongo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author cnstonefang@gmail.com
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PageLimit {

    int DEFAULT_LIMIT = 50;

    /**
     * 每次拉取时候的数量限制
     * @return
     */
    int limitPerFetch() default DEFAULT_LIMIT;

}