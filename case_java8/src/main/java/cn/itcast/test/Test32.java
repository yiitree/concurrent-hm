package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * 共享变量的可见性
 */
@Slf4j(topic = "c.Test32")
public class Test32 {


    /**
     * 方法1：
     * volatile易变---就是设置这个变量只在主存中才有，不进行缓存
     * 效率会降低
     */
    volatile static boolean run = true;


    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            while (run) {
                // 或者增加System.out.println也可以保证数据,因为print为synchronized操作
                System.out.println(run);
            }
        });
        t.start();
        sleep(1);
        // 线程t不会如预想的停下来
        run = false;
    }

    /**
     * 方法2：使用synchronized
     */
    // 加一个锁对象
    final static Object lock = new Object();
    static boolean run1 = true;

    public static void main2(String[] args) {
        Thread t = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    if (!run1) {
                        break;
                    }
                }
            }
        });
        t.start();
        sleep(1);
        // 线程t不会如预想的停下来
        run1 = false;
    }

}
