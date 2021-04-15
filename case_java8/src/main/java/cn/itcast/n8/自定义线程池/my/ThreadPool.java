package cn.itcast.n8.自定义线程池.my;

import javafx.concurrent.Worker;
import lombok.extern.slf4j.Slf4j;

/**
 * 线程池
 */
@Slf4j(topic = "c.ThreadPool")
class ThreadPool {

    /**
     * 阻塞队列
     */
    private final BlockingQueue<Worker> taskQueue;

    /**
     * 线程核心数
     */
    private int coreSize;

    ThreadPool(BlockingQueue<Worker> taskQueue) {
        this.taskQueue = taskQueue;
    }

}

