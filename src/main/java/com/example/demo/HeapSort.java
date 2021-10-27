package com.example.demo;

import java.util.Arrays;
import java.util.List;

/**
 * @author wuchonghua
 * @create 2021-09-10 13:29
 */
public class HeapSort {
    private int[] l;
    public HeapSort(int[] l) {
        this.l = l;
    }
    public int[] exec() {
        int len = l.length;
        int[] sortedL = Arrays.copyOf(l, len);
        int endIndex = len - 1;
        for (int i = (endIndex - 1) / 2; i >= 0; i--) {
            adjustHeap(sortedL, i, endIndex);
        }
        for (int i = 0; i < len; i++) {
            swap(sortedL, 0, len - i - 1);
            adjustHeap(sortedL, 0, len - i - 2);
        }
        return sortedL;
    }

    private void swap(int[] l, int index1, int index2) {
        int tmp = l[index1];
        l[index1] = l[index2];
        l[index2] = tmp;
    }

    public void adjustHeap(int[] l, int start, int end) {
        int tmp = l[start];
        int parent = start;
        for (int child = parent * 2 + 1; child <= end; child = child * 2 + 1) {
            if (child + 1 <= end && l[child + 1] > l[child]) {
                child++;
            }
            if (l[child] > tmp) {
                l[parent] = l[child];
                parent = child;
            } else {
                break;
            }
        }
        l[parent] = tmp;
    }
}
