package com.example.demo;

import java.util.Scanner;

/**
 * @author wuchonghua
 * @create 2023-09-12 10:49
 */
public class CCFTest20230502 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        String[] split = s.split(" ");
        int m = Integer.parseInt(split[0]);
        int n = Integer.parseInt(split[1]);
        int[][] Q = new int[m][n];
        int[][] K = new int[m][n];
        int[][] V = new int[m][n];
        init(Q, m, n, scanner);
        init(K, m, n, scanner);
        init(V, m, n, scanner);
        int[] W = new int[m];
        String[] split1 = scanner.nextLine().split(" ");
        for (int i = 0; i < m; i++) {
            W[i] = Integer.parseInt(split1[i]);
        }
        int[][] KT = new int[n][m];
        for (int i = 0; i < n; i++) {
            int[] row = new int[m];
            for (int j = 0; j < m; j++) {
                row[j] = K[j][i];
            }
            KT[i] = row;
        }

        int[][] QKT = multi(Q, KT);
        int[][] WQKT = new int[m][m];
        for (int i = 0; i < m; i++) {
            int[] row = new int[m];
            for (int j = 0; j < m; j++) {
                row[j] = QKT[i][j] * W[i];
            }
            WQKT[i] = row;
        }

        int[][] WQKTV = multi(WQKT, V);
        for (int i = 0 ; i < WQKTV.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < WQKTV[i].length; j++) {
                sb.append(WQKTV[i][j]).append(" ");
            }
            sb.deleteCharAt(sb.length() - 1);
            System.out.println(sb.toString());
        }

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