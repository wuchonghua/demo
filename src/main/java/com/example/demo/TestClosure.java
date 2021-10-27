package com.example.demo;

/**
 * @author wuchonghua
 * @create 2021-10-12 9:58
 */
public class TestClosure {

    public static void main(String[] args) {
        TestClosure t = new TestClosure();
        Func f = t.func(1); // 打印1
        System.out.println("---------");
        f.run(); // 实现了懒加载，在调用run()时才会打印2
        f.run(); // 只打印2，1只会打印一次
    }

    public Func func(int a) {
        System.out.println(a);
        return () -> {
          System.out.println(a + a);
        };
    }
}
