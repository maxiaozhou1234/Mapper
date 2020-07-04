package com.zhou.annoapp.annotation;

import com.lib.annotation.MapType;

/**
 * Created by mxz on 2020/7/3.
 */
@MapType(name = "testB", array = {"a", "b", "c"})
public class TestB {
    public TestB() {
        System.out.println(getClass().getName());
    }
}
