package cn.itcast.my.D;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: 曾睿
 * @Date: 2021/4/22 13:20
 */
public class Demo1 {
    public static final Sheep sheep = new Sheep(0);

    static class Sheep{
        private final AtomicInteger size;

        public Sheep(int size) {
            this.size = new AtomicInteger(0);
        }

        public AtomicInteger getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size.set(size);
        }

    }

    //悲观
    public synchronized static void increase1() {
        int value = sheep.getSize().intValue();
        sheep.setSize(value + 1);
    }

    //乐观(CAS)
    public static void increase2() {
        while (true){
            int expect = sheep.getSize().intValue();
            if(sheep.getSize().compareAndSet(expect,expect+1)){
                break;
            }
        }
    }

    public static void increase3() {
        while (true){
            int expect = sheep.getSize().intValue();
            if(sheep.getSize().compareAndSet(expect,expect+1)){
                break;
            }
        }
    }
}
