package cn.itcast.test.my;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 居民
 */
@Slf4j(topic = "c.People")
public class People extends Thread{
    @SneakyThrows
    @Override
    public void run() {
        MyMail myMail = MyMailbox.putMail();
        log.debug("居民在信箱中放入信件，此时信件编号为 id:{} 此时信件没有内容，下面开始读信（邮递员负责写信）",guardedObject.getId());
        // 等五秒拿信件读取信件内容-超时时间为5秒
        String content = myMail.get();
        log.debug("读取信件内容为:{}",content);
    }
}

