package cn.itcast.my;

/**
 * @Author: 曾睿
 * @Date: 2021/4/21 15:30
 */
public class MyJoin {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("开始");
            try {
                Thread.sleep(30000);
                System.out.println("结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
            thread.wait(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main开始");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
