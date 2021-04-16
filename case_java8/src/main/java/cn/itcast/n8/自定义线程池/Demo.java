package cn.itcast.n8.自定义线程池;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: 曾睿
 * @Date: 2021/4/15 16:59
 */
public class Demo {

    public static void main(String[] args) throws Exception {

        ExecutorService pool = Executors.newFixedThreadPool(3);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread");
            }
        };

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Runnable");
            }
        };

        Callable<Void> callable = new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("callable");
                return null;
            }
        };

        pool.submit(thread);
        pool.submit(runnable);
        pool.submit(callable);

    }

}
