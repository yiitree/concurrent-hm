package cn.itcast.n4;

import lombok.extern.slf4j.Slf4j;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * wait和notify使用
 */
@Slf4j(topic = "c.MyTestCorrectPosture2")
public class MyTestCorrectPosture2 {
    /**
     * 锁对象，设置为final
     */
    static final Object room = new Object();
    // 有没有烟
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;

    public static void main(String[] args) {
        // 线程1：必须要有烟才可以继续执行
        Thread t = new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没？[{}]", hasCigarette);
                if (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                        // 没有烟酒直接进入等待线程，等待别人叫自己再干活
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (hasCigarette) {
                    log.debug("可以开始干活了");
                }
            }
        }, "小南");

        // 其他线程直接干活
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (room) {
                    log.debug("可以开始干活了");
                }
            }, "其它人").start();
        }

        // 线程3：送烟线程
        sleep(1);
        new Thread(() -> {
            // 这里能不能加 synchronized (room)？
            synchronized (room) {
                hasCigarette = true;
                log.debug("烟到了噢！");
                t.start();
            }
        }, "送烟的").start();
    }

}
