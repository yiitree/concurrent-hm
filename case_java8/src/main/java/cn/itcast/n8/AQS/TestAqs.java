package cn.itcast.n8.AQS;

import lombok.extern.slf4j.Slf4j;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * 测试AQS
 */
@Slf4j(topic = "c.TestAqs")
public class TestAqs {
    public static void main(String[] args) {

        MyLock lock = new MyLock();

        new Thread(() -> {
            lock.lock();
            try {
                log.debug("locking...");
                sleep(1);
            } finally {
                log.debug("unlocking...");
                lock.unlock();
            }
        },"t1").start();

        new Thread(() -> {
            lock.lock();
            try {
                log.debug("locking...");
            } finally {
                log.debug("unlocking...");
                lock.unlock();
            }
        },"t2").start();
    }
}

