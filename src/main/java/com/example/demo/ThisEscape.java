package com.example.demo;

/**
 * @author wuchonghua
 * @create 2023-09-05 9:40
 */
public class ThisEscape {

    private static ThisEscape instance;

    private int value;

    public ThisEscape() {
        instance = this; // 发生 "this" 引用逸出
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        value = 42; // 可能不会被其他线程访问到
    }

    public static void main(String[] args) {
        new Thread(() -> {
            if (instance != null) {
                int val = instance.getValue(); // 其他线程可能会访问未完全构造的对象
                System.out.println(val);
            }
        }).start();

        new ThisEscape();
    } 

    public int getValue() {
        return value;
    }
}
