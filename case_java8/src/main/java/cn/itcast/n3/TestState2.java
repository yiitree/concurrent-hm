package cn.itcast.n3;

import cn.itcast.Constants;
import cn.itcast.n2.util.FileReader;

/**
 * java中状态和操作系统的状态对比
 * 操作系统的阻塞，其实是java中running
 */
public class TestState2 {
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            FileReader.read(Constants.MP4_FULL_PATH);
            FileReader.read(Constants.MP4_FULL_PATH);
            FileReader.read(Constants.MP4_FULL_PATH);
        }, "t1").start();

        Thread.sleep(1000);
        System.out.println("ok");
    }
}
