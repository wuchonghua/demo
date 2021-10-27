package com.example.demo;

import java.util.Arrays;
import java.util.List;

/**
 * @author wuchonghua
 * @create 2021-09-10 13:29
 */
public class QuickSort {
    private int[] l;
    public QuickSort(int[] l) {
        this.l = l;
    }
    public int[] exec() {
        int[] sortedL = Arrays.copyOf(l, l.length);
        quickSort(sortedL, 0, l.length - 1);
        return sortedL;
    }

    public void quickSort(int[] l, int left, int right) {
        if (left < right) {
            int i = partition(l, left, right);
            quickSort(l, left, i - 1);
            quickSort(l, i + 1, right);
        }
    }

    private int partition(int[] l, int left, int right) {
        int tmp = l[left];
        while (left < right) {
            while (left < right && l[right] >= tmp) {
                right--;
            }
            l[left] = l[right];
            while (left < right && l[left] <= tmp) {
                left++;
            }
            l[right] = l[left];
        }
        l[left] = tmp;
        return left;
    }
}
