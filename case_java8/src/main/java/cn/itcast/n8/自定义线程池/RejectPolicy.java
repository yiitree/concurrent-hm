package cn.itcast.n8.自定义线程池;

@FunctionalInterface // 拒绝策略
public interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}

