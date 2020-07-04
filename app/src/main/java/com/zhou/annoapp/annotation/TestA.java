package com.zhou.annoapp.annotation;


import com.lib.annotation.MapType;

/**
 * Created by mxz on 2020/7/3.
 */
@MapType(name = "testA", group = "love")
public class TestA {
    public TestA() {
        System.out.println(getClass().getName());
    }
}
