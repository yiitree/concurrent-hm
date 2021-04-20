package cn.itcast.n8.倒计时;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * 倒计时锁
 */
@Slf4j(topic = "c.TestCountDownLatch")
public class TestCountDownLatch {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        test1();
    }

    /**
     * 基本使用
     * @throws InterruptedException
     */
    private static void test1() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        new Thread(() -> {
            log.debug("begin...");
            sleep(1);
            log.debug("end...{}", latch.getCount());
            latch.countDown();
        }).start();

        new Thread(() -> {
            log.debug("begin...");
            sleep(2);
            log.debug("end...{}", latch.getCount());
            latch.countDown();
        }).start();

        new Thread(() -> {
            log.debug("begin...");
            sleep(1.5);
            log.debug("end...{}", latch.getCount());
            latch.countDown();
        }).start();

        log.debug("waiting...");
        latch.await();
        log.debug("wait end...");
    }

    /**
     * 配合线程池使用
     */
    private static void test2() {
        // 固定大小线程池：4
        ExecutorService service = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(3);
        service.submit(() -> {
            log.debug("begin...");
            sleep(1);
            log.debug("end...{}", latch.getCount());
            latch.countDown();
        });
        service.submit(() -> {
            log.debug("begin...");
            sleep(1.5);
            log.debug("end...{}", latch.getCount());
            latch.countDown();
        });
        service.submit(() -> {
            log.debug("begin...");
            sleep(2);
            log.debug("end...{}", latch.getCount());
            latch.countDown();
        });
        // 等待其他线程完成后再执行
        service.submit(()->{
            try {
                log.debug("waiting...");
                latch.await();
                log.debug("wait end...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 模拟开始加载
     * @throws InterruptedException
     */
    private static void test3() throws InterruptedException {
        // 模拟十个人加载游戏
        AtomicInteger num = new AtomicInteger(0);
        // 创建线程池：里面有10个线程
        ExecutorService service = Executors.newFixedThreadPool(10, (r) -> new Thread(r, "t" + num.getAndIncrement()));
        // 锁
        CountDownLatch latch = new CountDownLatch(10);

        String[] all = new String[10];
        Random r = new Random();
        for (int j = 0; j < 10; j++) {
            int x = j;
            // 让10个线程架加载
            service.submit(() -> {
                for (int i = 0; i <= 100; i++) {
                    try {
                        // 随机睡眠一会，最大100毫秒
                        Thread.sleep(r.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 创建一个数组，记录加载过程
                    all[x] = Thread.currentThread().getName() + "(" + (i + "%") + ")";
                    // 不换行 "\r" 表示是这次打印覆盖上一个打印结果
                    System.out.print("\r" + Arrays.toString(all));
                }
                // 每次加载完毕计数器-1
                latch.countDown();
            });
        }
        // 主线程等待，等所有加载完毕才开始执行
        latch.await();
        System.out.println("\n游戏开始...");
        service.shutdown();
    }

    /**
     * 没返回值的多线程
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void test4() throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        log.debug("begin");
        ExecutorService service = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(4);
        service.submit(() -> {
            restTemplate.getForObject("http://localhost:8080/order/{1}", Map.class, 1);
            latch.countDown();
        });
        service.submit(() -> {
            restTemplate.getForObject("http://localhost:8080/product/{1}", Map.class, 1);
            latch.countDown();
        });
        service.submit(() -> {
            restTemplate.getForObject("http://localhost:8080/product/{1}", Map.class, 2);
            latch.countDown();
        });
        service.submit(() -> {
            restTemplate.getForObject("http://localhost:8080/logistics/{1}", Map.class, 1);
            latch.countDown();
        });
        latch.await();
        log.debug("执行完毕");
        service.shutdown();
    }

    /**
     * 有返回值的多线程 - Future此时就不用CountDownLatch
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void test5() throws InterruptedException, ExecutionException {
        RestTemplate restTemplate = new RestTemplate();
        log.debug("begin");
        ExecutorService service = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(4);
        Future<Map<String,Object>> f1 = service.submit(() -> {
            Map<String, Object> response = restTemplate.getForObject("http://localhost:8080/order/{1}", Map.class, 1);
            return response;
        });
        Future<Map<String, Object>> f2 = service.submit(() -> {
            Map<String, Object> response1 = restTemplate.getForObject("http://localhost:8080/product/{1}", Map.class, 1);
            return response1;
        });
        Future<Map<String, Object>> f3 = service.submit(() -> {
            Map<String, Object> response1 = restTemplate.getForObject("http://localhost:8080/product/{1}", Map.class, 2);
            return response1;
        });
        Future<Map<String, Object>> f4 = service.submit(() -> {
            Map<String, Object> response3 = restTemplate.getForObject("http://localhost:8080/logistics/{1}", Map.class, 1);
            return response3;
        });

        System.out.println(f1.get());
        System.out.println(f2.get());
        System.out.println(f3.get());
        System.out.println(f4.get());
        log.debug("执行完毕");
        service.shutdown();
    }

}
