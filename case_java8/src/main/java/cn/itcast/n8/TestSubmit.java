package cn.itcast.n8;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程池
 * submit：提交任务，并返回任务结果
 * invokeAll：提交一组任务，并返回结果List<Future<T>>
 * invokeAny：提交一组任务，只有一个执行完毕，就停止其他线程，并返回第一个执行完的线程值
 */
@Slf4j(topic = "c.TestSubmit")
public class TestSubmit {

    public static void main(String[] args) throws Exception{
        ExecutorService pool = Executors.newFixedThreadPool(2);
        method3(pool);
    }

    /**
     * submit方法
     * 提交任务，并返回任务结果
     */
    private static void method1(ExecutorService pool) throws InterruptedException, ExecutionException {
        Future<String> future = pool.submit(
                // Callable---传入任务
                () -> {
                    log.debug("running");
                    Thread.sleep(1000);
                    return "ok";
                });
        log.debug("{}", future.get());
    }

    /**
     * invokeAll方法
     * 提交所有任务，并返回结果List<Future<T>>
     */
    private static void method2(ExecutorService pool) throws InterruptedException {
        List<Future<String>> futures = pool.invokeAll(Arrays.asList(
                // 任务1
                () -> {
                    log.debug("begin 1");
                    Thread.sleep(1000);
                    log.debug("end 1");
                    return "1";
                },
                // 任务2
                () -> {
                    log.debug("begin 2");
                    Thread.sleep(500);
                    log.debug("end 2");
                    return "2";
                },
                // 任务3
                () -> {
                    log.debug("begin 3");
                    Thread.sleep(2000);
                    log.debug("end 3");
                    return "3";
                }
        ));

        // 获取所有结果
        futures.forEach( f ->  {
            try {
                log.debug("{}", f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * invokeAny方法
     * 提交一组任务，只有一个执行完毕，就停止其他线程，并返回第一个执行完的线程值
     */
    private static void method3(ExecutorService pool) throws InterruptedException, ExecutionException {
        String result = pool.invokeAny(Arrays.asList(
                // 任务1
                () -> {
                    log.debug("begin 1");
                    Thread.sleep(1000);
                    log.debug("end 1");
                    return "1";
                },
                // 任务2
                () -> {
                    log.debug("begin 2");
                    Thread.sleep(500);
                    log.debug("end 2");
                    return "2";
                },
                // 任务3
                () -> {
                    log.debug("begin 3");
                    Thread.sleep(3000);
                    log.debug("end 3");
                    return "3";
                }
        ));
        log.debug("{}", result);
    }

}
