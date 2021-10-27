package com.example.demo;

import java.util.Arrays;

/**
 * @author wuchonghua
 * @create 2021-09-10 13:31
 */
public class SortMain {

    public static void main(String[] args) {
        int[] l = {24,35,97,45,8,34,23,13,54,78,0,98,28,24};
//        int[] l = {24};
//        HeadSort sort = new HeadSort(l);
        QuickSort sort = new QuickSort(l);
        int[] l2 = sort.exec();
        System.out.println(Arrays.toString(l2));
    }
}
