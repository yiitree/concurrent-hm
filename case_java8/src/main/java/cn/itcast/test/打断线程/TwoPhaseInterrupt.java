package cn.itcast.test.打断线程;

import lombok.extern.slf4j.Slf4j;

/**
 * 两阶段终止模式
 * 使用interrupt()方式进行打断
 */
@Slf4j(topic = "c.TwoPhaseInterrupt")
public class TwoPhaseInterrupt {
    // 监控线程
    private Thread monitorThread;

    /**
     * 启动监控线程
     */
    public void start() {
        monitorThread = new Thread(() -> {
            log.debug("开始任务");
            while (true) {
                Thread current = Thread.currentThread();
                // 是否被打断(正常阶段打断)
                if (current.isInterrupted()) {
                    log.debug("料理后事");
                    break;
                }
                try {
                    // 使用interrupt()此时睡眠就会终止，直接进入异常
                    Thread.sleep(100000);
                    log.debug("执行监控记录");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // false
                    System.out.println(current.isInterrupted());
                    current.interrupt();// 要手动修改为true
                    // true
                    System.out.println(current.isInterrupted());
                    log.debug("在监控的时候打断，此时重新设置打断标记");
                }
            }
        }, "monitor");
        monitorThread.start();
    }

    /**
     * 停止监控线程
     */
    public void stop() {
        // 使用interrupt()打断
        monitorThread.interrupt();
    }
}
