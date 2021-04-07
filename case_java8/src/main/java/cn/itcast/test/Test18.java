package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 调用wait、notify、notifyAll方法条件：必须是已经获得锁
 */
@Slf4j(topic = "c.Test18")
public class Test18 {
    static final Object lock = new Object();
    public static void main(String[] args) {


        // 错误调用
//        try {
//            lock.wait();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // 正确方法，必须是已经得到锁才可以调用方法
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
