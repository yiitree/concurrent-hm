package cn.itcast.n8.定时任务;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * Timer
 * 线程调度（类似计划任务）
 */
@Slf4j(topic = "c.TestTimer")
public class TestTimer {

    /**
     * 处理线程异常方法，
     * 任务中的异常不会自己抛出，出现异常会停止，但不会报出任何提示
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 方法1：在线程任务中自行try-catch捕获异常
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.schedule(() -> {
            try {
                log.debug("task1");
                int i = 1 / 0;
            } catch (Exception e) {
                log.error("error:", e);
            }
        }, 1, TimeUnit.SECONDS);

        // 方法2：不使用Runnable，使用Collectable接口（带有返回值的线程）
        // 使用future.get()方法是，如果有异常，得到的会是异常信息
        ExecutorService pool1 = Executors.newFixedThreadPool(1);
        final Future<Boolean> task1 = pool1.submit(() -> {
            log.debug("task1");
            int i = 1 / 0;
            return true;
        });
        task1.get();
    }

    /**
     * 基本timer使用
     * 过时了,单线程模式，而且有异常会停止后面线程
     */
    private static void method1() {
        Timer timer = new Timer();
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task 1");
                sleep(2);
            }
        };
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task 2");
            }
        };
        log.debug("start...");
        timer.schedule(task1, 1000);
        timer.schedule(task2, 1000);
    }

    /**
     * 使用ScheduledExecutorService
     * 带有调度功能的线程池
     */
    private static void method2() {
        // 固定核心线程大小
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        pool.schedule(() -> {
            try {
                log.debug("task1");
                int i = 1 / 0;
            } catch (Exception e) {
                log.error("error:", e);
            }
        }, 1, TimeUnit.SECONDS);

        pool.schedule(() -> {
            try {
                log.debug("task2");
            } catch (Exception e) {
                log.error("error:", e);
            }
        }, 1, TimeUnit.SECONDS);
    }

    /**
     * scheduleAtFixedRate()按照一定间隔来执行任务
     * 执行间隔,如果时间间隔比任务本身还短，就按照任务执行完毕后直接执行
     */
    private static void method3() {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        log.debug("start...");
        pool.scheduleAtFixedRate(() -> {
            log.debug("running...");
        }, 1, 1, TimeUnit.SECONDS);
    }

    /**
     * scheduleWithFixedDelay()按照一定间隔来执行任务
     * 按照上次线程执行完毕之后再计时延迟
     */
    private static void method4() {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        log.debug("start...");
        pool.scheduleWithFixedDelay(() -> {
            log.debug("running...");
        }, 1, 1, TimeUnit.SECONDS);
    }
}
