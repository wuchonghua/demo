package com.example.demo;

import cn.hutool.http.HttpUtil;

/**
 * @author wuchonghua
 * @create 2024-10-29 16:30
 */
public class TestHutoolHttp {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            String s = HttpUtil.get("http://httpbin.org/get");
            System.out.println(s);
            Thread.sleep(100);
        }

    }
}
