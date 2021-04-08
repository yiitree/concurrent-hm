package cn.itcast.test.固定顺序执行;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * 设置线程执行开始顺序
 * 其实就是单独设置一个标记，只有1先改过这个标记，2才会执行
 * 使用synchronized
 */
@Slf4j(topic = "c.Test25")
public class Test25 {

    static final Object lock = new Object();

    // 加一个flag，表示 t2 是否运行过
    static boolean t2runned = false;

    public static void main1(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                while (!t2runned) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("1");
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                log.debug("2");
                t2runned = true;
                lock.notify();
            }
        }, "t2");

        t1.start();
        t2.start();
    }

    static boolean flag = false;

    public static void main(String[] args) {

        Thread t = new Thread(()->{
                if(!flag){
                    LockSupport.park();
                }
                log.debug("执行");
        },"后");

        t.start();

        new Thread(()->{
            flag = true;
            log.debug("执行");
            LockSupport.unpark(t);
        },"先").start();

    }
}
