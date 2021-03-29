package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 两阶段终止模式
 */
@Slf4j(topic = "c.TwoPhaseTermination")
public class Test13 {
    public static void main(String[] args) throws InterruptedException {
        // 启动一个监视器
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();
        tpt.start();
        tpt.start();

        Thread.sleep(3500);
        log.debug("停止监控");
        tpt.stop();
    }
}

@Slf4j(topic = "c.TwoPhaseTermination")
class TwoPhaseTermination {
    // 监控线程
    private Thread monitorThread;
    // 停止标记
    private volatile boolean stop = false;
    // 判断是否执行过 start 方法
    private boolean starting = false;

    /**
     * 启动监控线程
     */
    public void start() {
        synchronized (this) {
            // false
            if (starting) {
                return;
            }
            starting = true;
        }
        monitorThread = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                // 是否被打断(正常阶段打断)
                if (stop) {
                    log.debug("料理后事");
                    break;
                }
                try {
                    // 在睡眠状态的时候被打断，此时只是改变标记，但是实际还在运行，等一个循环走正常打断步骤
                    Thread.sleep(1000);
                    log.debug("执行监控记录");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "monitor");
        monitorThread.start();
    }

    /**
     * 停止监控线程
     */
    public void stop() {
        stop = true;
        monitorThread.interrupt();
    }
}
