package com.example.demo;

/**
 * @author wuchonghua
 * @create 2022-01-04 16:24
 */
public class TestStringbuilder {

    public static void main(String[] args) {
        StringBuilder s = new StringBuilder();
        long t = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            s.append("41");
        }
        String s1 = s.toString();
        System.out.println(System.currentTimeMillis() - t);
    }
}
