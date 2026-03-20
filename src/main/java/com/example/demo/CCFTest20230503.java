package com.example.demo;

import java.util.Scanner;

/**
 * @author wuchonghua
 * @create 2023-09-12 10:49
 */
public class CCFTest20230503 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int byteNum = Integer.parseInt(scanner.nextLine());
        

    }

    private static void init(int[][] array, int m, int n, Scanner scanner) {
        for (int j = 0; j < m; j++) {
            int[] row = new int[n];
            String s1 = scanner.nextLine();
            String[] split1 = s1.split(" ");
            for (int k = 0; k < n; k++) {
                row[k] = Integer.parseInt(split1[k]);
            }
            array[j] = row;
        }
    }

    public static int[][] multi(int[][] matrixA, int[][] matrixB) {
        int m = matrixA.length;
        int n = matrixB[0].length;
        int p = matrixA[0].length;
        int[][] result = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int sum = 0;
                // 第i行 第j列
                for (int k = 0; k < p; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    
}