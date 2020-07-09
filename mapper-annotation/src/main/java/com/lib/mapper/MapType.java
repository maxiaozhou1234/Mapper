package com.lib.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mxz on 2020/7/3.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface MapType {
    /**
     * 存放组
     */
    String group() default Constant.defaultGroup;

    /**
     * 用于标记的 key
     */
    String name() default "";

    /**
     * 多个 key 共用一个类，可以使用 array 标记
     */
    String[] array() default {};
}
