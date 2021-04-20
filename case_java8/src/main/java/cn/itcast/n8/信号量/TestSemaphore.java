package cn.itcast.n8.信号量;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * 信号量
 */
@Slf4j(topic = "c.TestSemaphore")
public class TestSemaphore {
    public static void main(String[] args) {

        // 1. 创建 semaphore 对象 - 此时最大为3个同时进入
        Semaphore semaphore = new Semaphore(3);

        // 2. 10个线程同时运行
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    // 获得信号量
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    log.debug("running...");
                    sleep(1);
                    log.debug("end...");
                } finally {
                    // 释放信号量
                    semaphore.release();
                }
            }).start();
        }
    }
}
