package cn.itcast.test.打断线程;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * 使用LockSupport.park();打断
 */
@Slf4j(topic = "c.Test14")
public class Test14 {

    private static void test4() {
        Thread t1 = new Thread(() -> {
            log.debug("park...");
            LockSupport.park();
            for (int i = 0; i < 5; i++) {
                log.debug("打断状态：{}", Thread.interrupted());
            }
        });
        t1.start();
        sleep(1);
        t1.interrupt();
//        LockSupport.unpark(t1);
//         先park后interrupt
        sleep(10);
    }

    private static void test3() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("park...");
            LockSupport.park();
            log.debug("unpark...");
            log.debug("打断状态：{}", Thread.currentThread().isInterrupted());
        }, "t1");
        t1.start();

        sleep(1);
        t1.interrupt();

    }

    public static void main(String[] args) throws InterruptedException {
        test4();
    }
}
