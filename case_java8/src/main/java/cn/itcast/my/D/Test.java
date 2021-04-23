package cn.itcast.my.D;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cn.itcast.my.D.Demo1.*;

public class Test {

    //10 个线程模拟 10 个用户，它们同时调用递增方法修改 size 属性,记录每次的耗时结果
    public static void main(String[] args)  throws InterruptedException{
        long start = System.currentTimeMillis();


        ExecutorService executor = Executors.newFixedThreadPool(10);
        // 设置循环次数
        int callTime = 10000000;
        CountDownLatch countDownLatch = new CountDownLatch(callTime);

//        for(int i=0; i<callTime; i++) {
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    increase1();
//                    countDownLatch.countDown();
//                }
//            });
//        }

        for(int i=0; i<callTime; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    increase2();
                    long end = System.currentTimeMillis();
                    countDownLatch.countDown();// 循环一次-1
                }
            });
        }

//        for(int i=0; i<callTime; i++) {
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    increase3();
//                    countDownLatch.countDown();
//                }
//            });
//        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        executor.shutdown();

        System.out.println("调用次数：" + sheep.getSize().intValue());
        System.out.println("调用耗时: " + (end - start) );
    }

}
