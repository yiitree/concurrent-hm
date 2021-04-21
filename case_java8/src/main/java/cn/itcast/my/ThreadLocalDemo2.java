package cn.itcast.my;

/**
 * @Author: 曾睿
 * @Date: 2021/4/21 13:31
 */
public class ThreadLocalDemo2 {

    ThreadLocal<String> threadLocalA;

    public static void main(String[] args) throws InterruptedException {


        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("main");

        Thread a = new Thread(()->{
            threadLocal.set("a");
            System.out.println(threadLocal.get());
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread b = new Thread(()->{
            threadLocal.set("b");
            System.out.println(threadLocal.get());
        });
        a.start();
        b.start();
        System.out.println(threadLocal.get());
    }

}

