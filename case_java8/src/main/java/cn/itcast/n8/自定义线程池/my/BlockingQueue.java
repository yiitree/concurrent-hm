package cn.itcast.n8.自定义线程池.my;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.BlockingQueue")
public class BlockingQueue<T> {

    /**
     * 队列
     */
    private final Deque<T> deque = new ArrayDeque<>();

    /**
     * 队列大小
     */
    private final int size;

    /**
     * 锁ReentrantLock
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 取元素条件变量
     */
    private final Condition getCondition = lock.newCondition();

    /**
     * 放元素条件变量
     */
    private final Condition putCondition = lock.newCondition();

    public BlockingQueue(int size) {
        this.size = size;
    }

    /**
     * 放元素
     */
    public void put(T t,long timeout, TimeUnit unit){
        lock.lock();
        try{
            // 把时间统一转化为纳秒
            long nanos = unit.toNanos(timeout);
            // 使用while而不是使用if是因为，阻塞住了之后，下次是唤醒所有线程，还是需要抢夺，因此应该用while
            // 用if说明就是下次默认就抢到了，这就不对了
            while(deque.size() == size || nanos <= 0){
                // 返回等待时间 - 已经经过的时间 = 剩余时间
                nanos = putCondition.awaitNanos(nanos);
            }
            // 没有满
            // 放元素
            deque.addLast(t);
            // 唤醒取任务
            putCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 取元素
     */
    public T get(long timeout, TimeUnit unit){
        lock.lock();
        try{
            long nanos = unit.toNanos(timeout);
            // 队列为空
            while(deque.isEmpty() || nanos <=0){
                getCondition.await();
            }
            // 不为空
            final T t = deque.removeFirst();
            // 唤醒放任务
            putCondition.signal();
            return t;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取队列大小
     * @return 队列大小
     */
    public int size() {
        lock.lock();
        try {
            return deque.size();
        } finally {
            lock.unlock();
        }
    }

}
