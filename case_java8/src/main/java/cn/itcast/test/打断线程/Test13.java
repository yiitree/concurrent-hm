package cn.itcast.test.打断线程;

import lombok.extern.slf4j.Slf4j;


@Slf4j(topic = "c.TwoPhaseInterrupt")
public class Test13 {

    public static void main(String[] args) throws InterruptedException {
        // 启动一个监视器
        TwoPhaseInterrupt tpt = new TwoPhaseInterrupt();
        tpt.start();
        Thread.sleep(3500);
        log.debug("停止监控");
        tpt.stop();
    }

    public static void main1(String[] args) throws InterruptedException {
        // 启动一个监视器
        TwoPhaseVolatile tpt = new TwoPhaseVolatile();
        tpt.start();
        tpt.start();
        tpt.start();

        Thread.sleep(3500);
        log.debug("停止监控");
        tpt.stop();
    }
}
