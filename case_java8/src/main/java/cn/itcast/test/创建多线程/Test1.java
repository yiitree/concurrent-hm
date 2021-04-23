package cn.itcast.test.创建多线程;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j(topic = "c.Test1")
public class Test1 {

    public static void test2() {

        Thread t = new Thread(()->{ log.debug("running"); }, "t2");

        t.start();
    }
    public static void test1() {
        Thread t = new Thread(){
            @Override
            public void run() {
                log.debug("running");
            }
        };
        t.setName("t1");
        t.start();

    }

    public static void main1(String[] args) throws Exception{
        FutureTask futureTask=new FutureTask(new Callable() {
            @Override
            public String call() throws Exception {
                System.out.println("calld方法执行了");
                Thread.sleep(3000);
                System.out.println("calld方法执行完毕了");
                return "call方法返回值";
            }
        });
        FutureTask futureTask1=new FutureTask(new Callable() {
            @Override
            public String call() throws Exception {
                System.out.println("calld方法执行了1");
                Thread.sleep(3000);
                System.out.println("calld方法执行完毕了1");
                return "call方法返回值1";
            }
        });
        futureTask.run();
        futureTask1.run();
        System.out.println("获取返回值: " + futureTask.get());
        System.out.println("获取返回值1: " + futureTask1.get());
    }

    public static void main(String[] args) throws Exception {

        ExecutorService pool = Executors.newFixedThreadPool(10);
        FutureTask futureTask=new FutureTask(new Callable() {
            @Override
            public String call() throws Exception {
                System.out.println("calld方法执行了");
                Thread.sleep(3000);
                System.out.println("calld方法执行完毕了");
                return "call方法返回值";
            }
        });
        FutureTask futureTask1=new FutureTask(new Callable() {
            @Override
            public String call() throws Exception {
                System.out.println("calld方法执行了1");
                Thread.sleep(3000);
                System.out.println("calld方法执行完毕了1");
                return "call方法返回值1";
            }
        });

        pool.submit(futureTask);
        pool.submit(futureTask1);

        System.out.println("获取返回值: " + futureTask.get());
        System.out.println("获取返回值1: " + futureTask1.get());
    }

}
