package cn.itcast.test;

import cn.itcast.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

/**
 * wait和sleep方法
 */
@Slf4j(topic = "c.Test19")
public class Test19 {

    /**
     * 最好把锁对象设置为final，这样锁对象就不可变了，
     * 防止误操作变化后导致每次锁的都是不同对象
     */
    static final Object lock = new Object();
    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock) {
                log.debug("获得锁");
                try {
//                    Thread.sleep(20000);
                    lock.wait(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();

        Sleeper.sleep(1);
        synchronized (lock) {
            log.debug("获得锁");
        }
    }
}
