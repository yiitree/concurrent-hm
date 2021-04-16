package cn.itcast.n8.读写锁;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * 读写锁
 * 读-读：不阻塞
 * 写-写：阻塞
 * 读-写：阻塞
 */
@Slf4j(topic = "c.TestReadWriteLock")
public class TestReadWriteLock {
    public static void main(String[] args) throws InterruptedException {
        DataContainer dataContainer = new DataContainer();
        new Thread(() -> {
            dataContainer.write();
        }, "t1").start();
        new Thread(() -> {
            dataContainer.write();
        }, "t2").start();
    }
}

@Slf4j(topic = "c.DataContainer")
class DataContainer {
    private Object data;
    /**
     * ReentrantReadWriteLock锁，用于分别获得 读锁 和 写锁
     */
    private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    /**
     * 读锁
     */
    private final ReentrantReadWriteLock.ReadLock r = rw.readLock();
    /**
     * 写锁
     */
    private final ReentrantReadWriteLock.WriteLock w = rw.writeLock();

    public Object read() {
        log.debug("获取读锁...");
        r.lock();
        try {
            log.debug("读取");
            sleep(1);
            return data;
        } finally {
            log.debug("释放读锁...");
            r.unlock();
        }
    }

    public void write() {
        log.debug("获取写锁...");
        w.lock();
        try {
            log.debug("写入");
            sleep(1);
        } finally {
            log.debug("释放写锁...");
            w.unlock();
        }
    }
}
