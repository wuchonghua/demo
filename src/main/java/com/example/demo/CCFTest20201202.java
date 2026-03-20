package com.example.demo;

import java.util.Scanner;

/**
 * @author wuchonghua
 * @create 2023-09-12 10:49
 */
public class CCFTest20201202 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = Integer.parseInt(scanner.nextLine());
        Pair[] pairs = new Pair[m];
        for (int i = 0; i < m; i++) {
            String s = scanner.nextLine();
            String[] split = s.split(" ");
            Pair pair = new Pair(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            pairs[i] = pair;
        }
        int result = -1;
        int maxCount = -1;
        for (Pair predictThreshold : pairs) {
            int count = 0;
            for (Pair pair : pairs) {
                if (predict(pair.y, pair.result, predictThreshold.y)) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                result = predictThreshold.y;
            } else if (count == maxCount && result < predictThreshold.y) {
                result = predictThreshold.y;
            }
        }

        System.out.println(result);
    }

    private static boolean predict(int y, int result, int threshold) {
        return (y >= threshold && result == 1) || (y < threshold && result == 0);
    }

}



class Pair {
    int y;
    int result;

    public Pair(int y, int result) {
        this.y = y;
        this.result = result;
    }
}
