package cn.itcast.my;

/**
 * @Author: 曾睿
 * @Date: 2021/4/21 15:30
 */
public class MyJoin {

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(() -> {
            System.out.println(Thread.interrupted());
            System.out.println("开始");
            try {
                Thread.sleep(3000);
                System.out.println("结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.interrupted());
        });

        thread.start();
        try {
            Thread.sleep(1000);
            thread.join();
            Thread.sleep(1000);
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main开始");

    }
}
