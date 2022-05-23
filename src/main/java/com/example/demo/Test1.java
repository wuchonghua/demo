package com.example.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuchonghua
 * @create 2022-05-23 14:04
 */
public class Test1 {

    public static Map<String, String> m = new HashMap<>();
    public static void init(String key, String value) {
        m.clear();
        m.put(key, value);
    }
}
