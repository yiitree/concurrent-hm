package cn.itcast.my;

/**
 * @Author: 曾睿
 * @Date: 2021/4/21 13:31
 */
public class ThreadLocalDemo {

    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        new Thread(new ThreadA(threadLocal)).start();
        new Thread(new ThreadA.ThreadB(threadLocal)).start();
    }


    // ThreadA
    static class ThreadA implements Runnable {
        private final ThreadLocal<String> threadLocalA;
        public ThreadA(ThreadLocal<String> threadLocal) {
            this.threadLocalA = threadLocal;
        }
        @Override
        public void run() {
            threadLocalA.set("A");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ThreadA输出：" + threadLocalA.get());
        }

        // 在ThreadA中创建ThreadB
        static class ThreadB implements Runnable  {
            private final ThreadLocal<String> threadLocalB;
            public ThreadB(ThreadLocal<String> threadLocal) {
                this.threadLocalB = threadLocal;
            }
            @Override
            public void run() {
                threadLocalB.set("B");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("ThreadB输出：" + threadLocalB.get());
            }
        }

    }
}

// 输出：
//ThreadA输出：A
//ThreadB输出：B
