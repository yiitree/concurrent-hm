package cn.itcast.n8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * 多线程反复运行
 */
@Slf4j(topic = "c.TestCyclicBarrier")
public class TestCyclicBarrier {


    /**
     * CyclicBarrier
     */
    public static void main(String[] args) {
        test2();
    }

    /**
     * 反复运行多次
     * CountDownLatch
     * 缺点：CountDownLatch不可以被重用，因此需要反复创建该对象
     * 这里要求线程数和计数不需要一致，因为每次都是到零后停止，然后重新创建线程，就不会出现先执行t1t3线程了
     */
    private static void test1() {

        ExecutorService service = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 3; i++) {
            // 不可以被重用，需要反复创建该对象  设置计数个数后就无法修改
            CountDownLatch latch = new CountDownLatch(2);
            service.submit(() -> {
                log.debug("task1 start...");
                sleep(1);
                log.debug("task1 end...");
                latch.countDown();
            });
            service.submit(() -> {
                log.debug("task2 start...");
                sleep(2);
                log.debug("task2 end...");
                latch.countDown();
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("task1 task2 finish...");
        }
        service.shutdown();
    }

    /**
     * 线程数和计数要一样，否则就会执行三个线程，
     * t1-task1 1s
     * t2-task2 2s
     * t3-task1 3s
     * 这样就会指的t1 t3先执行完毕，而不是t1t2循环执行
     */
    public static void test2() {
        ExecutorService service = Executors.newFixedThreadPool(20);
        CyclicBarrier barrier = new CyclicBarrier(
                // 设置计数
                2,
                // 设置任务 - 此任务是在循环任务结束后，执行一次
                () -> log.debug("task1, task2 finish...")
        );
        // task1  task2  task1
        for (int i = 0; i < 3; i++) {
            service.submit(() -> {
                log.debug("task1 begin...");
                sleep(1);
                log.debug("task1 end...");
                try {
                    barrier.await(); // 2-1=1
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            service.submit(() -> {
                log.debug("task2 begin...");
                sleep(2);
                log.debug("task2 end...");
                try {
                    barrier.await(); // 1-1=0
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        service.shutdown();
    }
}
