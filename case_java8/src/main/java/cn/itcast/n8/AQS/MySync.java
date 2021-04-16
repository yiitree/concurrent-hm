package cn.itcast.n8.AQS;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;

/**
 * 独占锁  同步器类
 * 自定义同步器，继承AQS，只需重写：
 * ---tryAcquire尝试获得锁
 * ---tryRelease尝试释放锁
 * ---isHeldExclusively是否持有独占锁
 */
public class MySync extends AbstractQueuedSynchronizer {
    /**
     * 尝试获得锁
     *
     * @param arg
     * @return
     */
    @Override
    protected boolean tryAcquire(int arg) {
        // 0:未加锁 1：加锁，先设置锁状态为1，因为是竞争的，所以使用compareAndSetState
        if (compareAndSetState(0, 1)) {
            // 加上了锁，并设置 owner 为当前线程
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }

    /**
     * 尝试释放锁
     *
     * @param arg
     * @return
     */
    @Override
    protected boolean tryRelease(int arg) {
        setExclusiveOwnerThread(null);
        // state对象是volatile的，所以应该写在后面，保证前面是线程可见的
        setState(0);
        return true;
    }

    /**
     * 是否持有锁
     *
     * @return
     */
    @Override
    protected boolean isHeldExclusively() {
        return getState() == 1;
    }

    /**
     * 返回条件变量
     * @return
     */
    public Condition newCondition() {
        return new AbstractQueuedSynchronizer.ConditionObject();
    }
}
