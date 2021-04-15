package cn.itcast.n8.自定义线程池;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阻塞队列
 * 其实相当于一个list队列，先进先出而已，里面元素泛型表示，不和具体实现相关
 * 只是：放元素、取元素
 * 另外是多个线程取元素，因此加上了锁，保证操作是安全的
 * @param <T>
 */
@Slf4j(topic = "c.BlockingQueue")
public class BlockingQueue<T> {

    /**
     * 任务队列
     * ArrayDeque:双向链表
     * LinkedList:单向链表
     * 使用双向链表，因为是一个队列，想要做到，先进先出，头取尾放
     */
    private final Deque<T> queue = new ArrayDeque<>();

    /**
     * 队列的容量
     */
    private final int capacity;

    /**
     * 锁
     * 由于是有存、取两个线程，所以使用ReentrantLock，这样阻塞唤醒的时候就可以指定唤醒等待的队列
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 放元素：
     * 生产者条件变量
     * 唤醒的时候可以指定唤醒哪个队列
     */
    private final Condition fullWaitSet = lock.newCondition();

    /**
     * 取线程：
     * 消费者条件变量
     */
    private final Condition emptyWaitSet = lock.newCondition();

    /**
     * 构造函数，创建一个指定大小的队列
     * @param capacity
     */
    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * 阻塞添加
     * @param task 生产者线程
     */
    public void put(T task) {
        lock.lock();
        try {
            // 线程池满了，则不行添加了
            while (queue.size() == capacity) {
                try {
                    log.debug("等待加入任务队列 {} ...", task);
                    // 阻塞，不能放线了
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列 {}", task);
            // 放在队列尾部
            queue.addLast(task);
            // 唤醒消费者线程
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞获取
     * @return 消费者线程
     */
    public T take() {
        // 获取的时候要保证安全，这个线程要从线程池里拿线程的时候其他线程不能一起拿
        lock.lock();
        try {
            // 线程池中是否为空，如果空了说明没有线程可用了
            while (queue.isEmpty()) {
                try {
                    // 如果队列中没有线程，则进行等待
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 取出队列头部元素
            T t = queue.removeFirst();
            // 唤醒生产线程
            fullWaitSet.signal();
            return t;
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
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 带超时时间阻塞添加
     * @param task 元素
     * @param timeout 超时时间
     * @param timeUnit 时间转换工具
     * @return 是否成功
     */
    public boolean offer(T task, long timeout, TimeUnit timeUnit) {
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            while (queue.size() == capacity) {
                try {
                    if(nanos <= 0) {
                        return false;
                    }
                    log.debug("等待加入任务队列 {} ...", task);
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列 {}", task);
            queue.addLast(task);
            emptyWaitSet.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 带超时阻塞获取
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 元素
     */
    public T poll(long timeout, TimeUnit unit) {
        lock.lock();
        try {
            // 将 timeout 统一转换为 纳秒
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()) {
                try {
                    // 判断剩余时间<0说明等待超时
                    if (nanos <= 0) {
                        return null;
                    }
                    // 返回等待剩余时间
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 尝试把多余任务放到队列中，
     * 此时就需要实现拒绝策略
     * @param rejectPolicy
     * @param task
     */
    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try {
            // 判断队列是否满
            if(queue.size() == capacity) {
                rejectPolicy.reject(this, task);
            } else {  // 有空闲
                log.debug("加入任务队列 {}", task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
