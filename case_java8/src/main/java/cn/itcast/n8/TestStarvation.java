package cn.itcast.n8;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程饥饿：
 * 固定两个线程，所做的事情是有先后顺序的，（任务里面还有任务）
 * 假如全部线程都做第一步，没人有资源做第二步了，大家就都停顿了
 */
@Slf4j(topic = "c.TestDeadLock")
public class TestStarvation {

    static final List<String> MENU = Arrays.asList("地三鲜", "宫保鸡丁", "辣子鸡丁", "烤鸡翅");
    static Random RANDOM = new Random();
    // 做菜
    static String cooking() {
        return MENU.get(RANDOM.nextInt(MENU.size()));
    }

    public static void main(String[] args) {
        // 点餐
        ExecutorService waiterPool = Executors.newFixedThreadPool(1);
        // 做菜
        ExecutorService cookPool = Executors.newFixedThreadPool(1);

        // 点餐任务里面有做菜任务
        waiterPool.execute(() -> {
            log.debug("处理点餐...");
            // 处理点餐完毕后就去提交做菜任务
            Future<String> f = cookPool.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜: {}", f.get());
            } catch (InterruptedException | ExecutionException e  ) {
                e.printStackTrace();
            }
        });

        waiterPool.execute(() -> {
            log.debug("处理点餐...");
            Future<String> f = cookPool.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜: {}", f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

    }
}
