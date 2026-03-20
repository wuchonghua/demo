package com.example.demo;

import org.apache.directory.api.util.Strings;

import java.util.Arrays;

/**
 * @author wuchonghua
 * @create 2023-04-23 16:33
 */
public class Test2 {
    public static void main(String[] args) {
        int n = 2147483647;
        if (n < 10) {
            System.out.println(n);
        }
        n -= 9;
        int i = 2;
        int j = 10;
        // 10 + 2 * 9 * 10 + 3 * 9 * 100
        while (n - (i * 9 * j) > 0) {
            n = n - (i * 9 * j);
            i += 1;
            j *= 10;
        }
        // 2700 / 3 = 900  900 - 1 = 899  899 + 100 = 999
        int currentNum = (n / i) - 1 + j;
        if (n % i == 0) {
            System.out.println(currentNum % 10);
        } else {
            String s = String.valueOf(currentNum + 1);
            System.out.println(Integer.parseInt(String.valueOf(s.charAt(((n % i) - 1)))));
        }

        StringBuilder sb = new StringBuilder();
        for (int k = 0; k <= 10000; k++) {
            sb.append(k);
        }
        System.out.println(sb.toString().substring(2891, 2900));
    }
}
