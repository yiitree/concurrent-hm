package cn.itcast.n8.自定义线程池;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 */
@Slf4j(topic = "c.ThreadPool")
class ThreadPool {
    // 任务队列---自定义的任务阻塞队列
    private final BlockingQueue<Runnable> taskQueue;

    // 线程集合---其实是Thread包装成Worder类
    private final HashSet<Worker> workers = new HashSet<>();

    // 核心线程数---最大线程个数
    private final int coreSize;

    // 获取任务时的超时时间---线程超时时间
    private final long timeout;

    // 阻塞时间单位
    private final TimeUnit timeUnit;

    private final RejectPolicy<Runnable> rejectPolicy;

    // 执行任务
    /**
     * 构造方法
     * @param coreSize
     * @param timeout
     * @param timeUnit
     * @param queueCapcity
     * @param rejectPolicy
     */
    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapcity, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapcity);
        this.rejectPolicy = rejectPolicy;
    }

    /**
     * 执行任务方法
     * @param task 任务对象
     */
    public void execute(Runnable task) {
        synchronized (workers) {
            // 当任务数没有超过 coreSize 时，直接交给 worker 对象执行
            if(workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增 worker{}, {}", worker, task);
                workers.add(worker);
                // 开始线程执行
                worker.start();
            } else {
                // 如果任务数超过 coreSize 时，加入任务队列暂存
//                taskQueue.put(task);
                // 1) 死等
                // 2) 带超时等待
                // 3) 让调用者放弃任务执行
                // 4) 让调用者抛出异常
                // 5) 让调用者自己执行任务
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }

    class Worker extends Thread{

        // 任务对象
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        /**
         * 执行任务
         */
        @Override
        public void run() {
            // 1) 当 task 不为空，执行任务
            // 2) 当 task 执行完毕，再接着从任务队列获取任务并执行
//            while(task != null || (task = taskQueue.take()) != null) {

            while(task != null || (task = taskQueue.poll(timeout, timeUnit)) != null) {
                try {
                    log.debug("正在执行...{}", task);
                    // 开启任务
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            // 执行完了，就把workers移除掉
            synchronized (workers) {
                log.debug("worker 被移除{}", this);
                workers.remove(this);
            }
        }
    }
}

