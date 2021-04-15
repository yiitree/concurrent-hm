package cn.itcast.n8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 线程池
 * shutDown:
 *  不会接收新任务，但已提交任务会执行完，不会阻塞调用线程的执行
 * awaitTermination:
 *  阻塞主线程，超时时间n秒，n秒后或shutdown()执行完毕后才会继续向下执行
 * shutdownNow：
 *  线程池中状态设置为stop状态、不会接受新任务，正在执行的任务也会打断
 *  正在执行的任务会使用interrupt()方法打断，队列中的任务作为返回结果
 */
@Slf4j(topic = "c.TestShutDown")
public class TestShutDown {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<Integer> result1 = pool.submit(() -> {
            log.debug("task 1 running...");
            Thread.sleep(1000);
            log.debug("task 1 finish...");
            return 1;
        });
        Future<Integer> result2 = pool.submit(() -> {
            log.debug("task 2 running...");
            Thread.sleep(1000);
            log.debug("task 2 finish...");
            return 2;
        });
        // 线程池中只有两个线程，因为这个线程为进入阻塞队列
        Future<Integer> result3 = pool.submit(() -> {
            log.debug("task 3 running...");
            Thread.sleep(1000);
            log.debug("task 3 finish...");
            return 3;
        });

        log.debug("shutdown");
        pool.shutdown();
        // 调用shutdown方法不会阻塞主线程继续向下执行
        System.out.println("shut");
        // 阻塞主线程，超时时间3秒，3秒后或shutdown()执行完毕后才会继续向下执行
        pool.awaitTermination(3, TimeUnit.SECONDS);
        log.debug("awaitTermination");

//        List<Runnable> runnables = pool.shutdownNow();
//        log.debug("other.... {}" , runnables);

    }
}
