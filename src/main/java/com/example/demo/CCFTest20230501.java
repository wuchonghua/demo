package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author wuchonghua
 * @create 2023-09-12 10:49
 */
public class CCFTest20230501 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        Map<String, Integer> map = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 1; j <= 8; j++) {
                sb.append(scanner.nextLine());
            }
            String s = sb.toString();
            if (map.containsKey(s)) {
                map.put(s, map.get(s) + 1);
            } else {
                map.put(s, 1);
            }
            System.out.println(map.get(s));
        }
    }

}