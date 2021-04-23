package cn.itcast.n8.信号量.my;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicIntegerArray;

@Slf4j(topic = "c.Pool")
class Pool {

    // 1. 连接池大小
    private final int poolSize;

    // 2. 连接对象数组
    private final Connection[] connections;

    // 3. 连接状态数组 0 表示空闲， 1 表示繁忙
    private final AtomicIntegerArray states;

    // 信号量 --- 最大连接数
    private final Semaphore semaphore;

    // 4. 构造方法初始化
    public Pool(int poolSize) {
        this.poolSize = poolSize;
        // 让许可数与资源数一致 - 最大连接数量为连接池中连接数量
        this.semaphore = new Semaphore(poolSize);
        this.connections = new Connection[poolSize];
        this.states = new AtomicIntegerArray(new int[poolSize]);
        for (int i = 0; i < poolSize; i++) {
            connections[i] = new MockConnection("连接" + (i + 1));
        }
    }

    // 5. 借连接
    public Connection borrow() {// t1, t2, t3
        // 获取许可
        try {
            semaphore.acquire(); // 没有许可的线程，在此等待
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < poolSize; i++) {
            // 获取空闲连接
            if (states.get(i) == 0) {
                if (states.compareAndSet(i, 0, 1)) {
                    log.debug("borrow {}", connections[i]);
                    return connections[i];
                }
            }
        }
        // 不会执行到这里
        return null;
    }

    // 6. 归还连接
    public void free(Connection conn) {
        for (int i = 0; i < poolSize; i++) {
            if (connections[i] == conn) {
                states.set(i, 0);
                log.debug("free {}", conn);
                // 归还连接
                semaphore.release();
                break;
            }
        }
    }


}
