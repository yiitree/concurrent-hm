package cn.itcast.test.交替执行;

import lombok.extern.slf4j.Slf4j;

/**
 * 交替输出
 * 线程1 输出a 5 次，
 * 线程2 输出b 5 次，
 * 线程3 输出c 5 次。
 * 现在要求输出abc abc abc abc abc 怎么实现
 */
@Slf4j(topic = "c.Test27my")
public class Test27my {

    static Object lock = new Object();

    static String flag = "a";

    public static void main(String[] args) {

        // a
        new Thread(()->{
            synchronized (lock){
                int total = 5;
                while (total>0){
                    if(!"a".equals(flag)){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        log.debug("a");
                        flag = "b";
                        total--;
                        lock.notifyAll();
                    }
                }
            }
        }).start();
        // b
        new Thread(()->{
            synchronized (lock){
                int total = 5;
                while (total>0){
                    if(!"b".equals(flag)){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        log.debug("b");
                        flag = "c";
                        total--;
                        lock.notifyAll();
                    }
                }
            }
        }).start();
        // c
        new Thread(()->{
            synchronized (lock){
                int total = 5;
                while (total>0){
                    if(!"c".equals(flag)){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        log.debug("c");
                        flag = "a";
                        total--;
                        lock.notifyAll();
                    }
                }
            }
        }).start();
    }

}
