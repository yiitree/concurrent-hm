package cn.itcast.test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 原子基本类型
 */
public class Test34 {
    public static void main(String[] args) {

        AtomicInteger i = new AtomicInteger(1);

//        System.out.println(i.getAndIncrement()); // i++ 1 i.get()：2
//        System.out.println(i.incrementAndGet()); // ++i 2 i.get()：2

//        System.out.println(i.getAndAdd(2)); // 1 i.get()：3
//        System.out.println(i.addAndGet(2)); // 3 i.get()：3

//        System.out.println(i.getAndUpdate(v -> v * 3)); // 1 i.get() 3
//        System.out.println(i.updateAndGet(v -> v * 3)); // 3 i.get() 3

        // 获取并计算：(i=1, p为i初始值, x为第一个参数：10; 返回i的初始值：1, i.get()：11)
//        System.out.println(i.getAndAccumulate(10, (p, x) -> p + x));
//        System.out.println(i.accumulateAndGet(10, (p, x) -> p + x)); // 11 i.get() 11


        // 汇总
//        System.out.println(i.getAndSet(10));// 1 i.get() 10
//        System.out.println(i.getAndIncrement()); // i++
//        System.out.println(i.getAndDecrement()); // i--
//        System.out.println(i.getAndAdd(5)); // i+5
//        System.out.println(i.getAndUpdate(v -> v * 3));
//        // 获取并计算：(i=1, p为i初始值, x为第一个参数：10; 返回i的初始值：1, i.get()：11)
//        System.out.println(i.getAndAccumulate(10, (p, x) -> p + x));
        System.out.println(i.get());
    }

}
