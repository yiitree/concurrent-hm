package cn.itcast.n4;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 曾睿
 * @Date: 2021/3/29 21:04
 */
@Slf4j(topic = "c.Demo")
public class Demo {

    private static Integer i = 0;
    public static void main(String[] args) throws InterruptedException {
        List<Thread> list = new ArrayList<>();

        for (int j = 0; j < 2; j++) {
            Thread thread = new Thread(() -> {

                for (int k = 0; k < 5000; k++) {
                    synchronized (i) {
                        i++;
                    }
                }

            }, "" + j);
            list.add(thread);
        }

        // 启动
        list.stream().forEach(t -> t.start());
        // 等待两个线程全部执行完
        list.stream().forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        log.debug("{}", i);
    }

}
