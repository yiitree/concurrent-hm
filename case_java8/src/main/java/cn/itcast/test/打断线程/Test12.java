package cn.itcast.test.打断线程;

import lombok.extern.slf4j.Slf4j;

/**
 * 使用interrupt打断
 */
@Slf4j(topic = "c.Test12")
public class Test12 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while(true) {
                boolean interrupted = Thread.currentThread().isInterrupted();
                if(interrupted) {
                    log.debug("被打断了, 退出循环");
                    break;
                }
            }
        }, "t1");
        t1.start();

        Thread.sleep(1000);
        log.debug("interrupt");
        // 打断正在运行的线程，只是标记有人要打断我，但是实际上并不会停止运行
        t1.interrupt();
    }
}
