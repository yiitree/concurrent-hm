package cn.itcast.test;

/**
 * 主要是用来查看同步字节码文件的
 * @Author: 曾睿
 * @Date: 2021/4/6 16:06
 */
public class Dssss {

    static final Object lock = new Object();
    static int counter = 0;

    public static void main(String[] args) {
        synchronized (lock){
            counter++;
        }
    }
}
