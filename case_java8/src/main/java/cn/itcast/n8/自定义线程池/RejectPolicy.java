package cn.itcast.n8.自定义线程池;

/**
 * 拒绝策略
 * @param <T>
 */
@FunctionalInterface
public interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}

