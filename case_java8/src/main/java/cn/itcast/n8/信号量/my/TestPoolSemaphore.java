package cn.itcast.n8.信号量.my;

import java.sql.Connection;

/**
 * 使用Semaphore控制连接池
 */
public class TestPoolSemaphore {
    public static void main(String[] args) {
        // 设置一个数据库连接池
        Pool pool = new Pool(2);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                Connection conn = pool.borrow();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pool.free(conn);
            }).start();
        }
    }
}

