package com.zhou.annoapp;

import com.lib.annotation.MapCollection;

import org.junit.Test;

/**
 * Created by zhou on 2020/7/4.
 */

public class ModuleTest {

    @Test
    public void testMap() {
        Class a = MapCollection.findItem("testA");
        System.out.println(a);
        build(a);

        a = MapCollection.findItem("love", "testA");
        System.out.println(a);
        build(a);

        a = MapCollection.findItem("_default", "testB", false);
        System.out.println(a);
        build(a);

        a = MapCollection.findItem("kk", "testB", true);
        System.out.println(a);
        build(a);

        System.out.println(MapCollection.string());
    }

    private void build(Class clz) {
        if (clz == null)
            return;
        try {
            clz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
