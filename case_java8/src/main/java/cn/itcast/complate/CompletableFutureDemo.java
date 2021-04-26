package cn.itcast.complate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @Author: 曾睿
 * @Date: 2021/4/25 10:36
 */
public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 提交一个任务，返回CompletableFuture
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                System.out.println("=============>异步线程开始...");
                System.out.println("=============>异步线程为：" + Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("=============>异步线程结束...");
                return "supplierResult";
            }
        });

        // 提交一个任务，返回CompletableFuture
        CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                System.out.println("=============>异步线程开始1...");
                System.out.println("=============>异步线程为1：" + Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("=============>异步线程结束1...");
                return "supplierResult";
            }
        });

        // 提交一个任务，返回CompletableFuture
        CompletableFuture<String> completableFuture2 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                System.out.println("=============>异步线程开始2...");
                System.out.println("=============>异步线程为2：" + Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("=============>异步线程结束2...");
                return "supplierResult";
            }
        });


        // 阻塞获取结果
//        System.out.println("异步结果是:" + completableFuture.get());
//        System.out.println("异步结果是1:" + completableFuture1.get());
//        System.out.println("异步结果是2:" + completableFuture2.get());
//        System.out.println("main结束");

        completableFuture.thenAccept((result) -> {
            System.out.println("异步结果是:" + result);
        });
        completableFuture1.thenAccept((result) -> {
            System.out.println("异步结果是1:" + result);
        });
        completableFuture2.thenAccept((result) -> {
            System.out.println("异步结果是2:" + result);
        });
        TimeUnit.SECONDS.sleep(20);
    }
}
