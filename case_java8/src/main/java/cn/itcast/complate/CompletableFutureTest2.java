package cn.itcast.complate;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CompletableFutureTest2 {

    private static final AtomicInteger count = new AtomicInteger(2);

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // 准备10个线程
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        long start = System.currentTimeMillis();

        // 模拟处理订单
        CompletableFuture<Void> dealOrder = CompletableFuture.runAsync(() -> {
            // 模拟任务耗时 0~4秒
            try {
                int seconds = ThreadLocalRandom.current().nextInt(5);
                TimeUnit.SECONDS.sleep(seconds);
                System.out.println("task is completed! cost:" + seconds + "s left: " + count.decrementAndGet());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, executorService);

        // 模拟处理库存
        CompletableFuture<Void> dealStock = CompletableFuture.runAsync(() -> {
            // 模拟任务耗时 0~4秒
            try {
                int seconds = ThreadLocalRandom.current().nextInt(5);
                TimeUnit.SECONDS.sleep(seconds);
                System.out.println("task is completed! cost:" + seconds + "s left: " + count.decrementAndGet());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, executorService);

        // 可变参数，可以传任意个CompletableFuture，阻塞等待所有任务完成
        CompletableFuture.allOf(dealOrder, dealStock).get();

        // 最终全部任务完成，总耗时取决于最耗时的任务时长
        System.out.println("all task is completed! cost: " + (System.currentTimeMillis() - start) + "ms");
    }
}
