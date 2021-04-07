package cn.itcast.test.my;

import lombok.extern.slf4j.Slf4j;

/**
 * 居民
 */
@Slf4j(topic = "c.People")
public class People extends Thread{

    @Override
    public void run() {
        MyMail myMail = MyMailbox.putMail();
        log.debug("居民在信箱中放入信件，此时信件没有内容");
        // 读取信件内容
        String content = null;
        try {
            content = myMail.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("读取信件内容为:{}",content);
    }
}

