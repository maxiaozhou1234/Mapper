package com.zhou.annoapp.annotation;

import com.lib.mapper.MapType;

/**
 * Created by mxz on 2020/7/3.
 */
@MapType(name = "testC")
public class TestC {
    public TestC() {
        System.out.println(getClass().getName());
    }
}
