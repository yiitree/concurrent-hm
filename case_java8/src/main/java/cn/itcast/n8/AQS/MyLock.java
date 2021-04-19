package cn.itcast.n8.AQS;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 自定义锁（不可重入锁,自己加的锁也会挡住自己）
 * 实现Lock方法其实是使用sync的内部方法
 *
 */
public class MyLock implements Lock {

    /**
     * 锁的功能，基本都是由同步器来实现
     * 同步器
     */
    private final MySync sync = new MySync();

    /**
     * 加锁（不成功会进入等待队列）
     * 0:未加锁 1：加锁
     * --- AbstractQueuedSynchronizer实现
     */
    @Override
    public void lock() {
        sync.acquire(1);
    }

    /**
     * 加锁（可打断）
     * --- AbstractQueuedSynchronizer实现
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    /**
     * 尝试加锁（尝试一次，成功返回true，失败返回false）
     * -- MySync实现
     * @return
     */
    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    /**
     * 尝试加锁（带超时）
     * --- AbstractQueuedSynchronizer实现
     * @param time
     * @param unit
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    /**
     * 解锁
     * --- AbstractQueuedSynchronizer实现
     */
    @Override
    public void unlock() {
        // 释放锁，并唤醒其他阻塞队列
        sync.release(1);
    }

    /**
     * 获得条件变量
     * --- AbstractQueuedSynchronizer实现
     * @return
     */
    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
