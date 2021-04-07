package cn.itcast.test.my;

import cn.itcast.test.mail信件案例.GuardedObject;
import cn.itcast.test.mail信件案例.Mailboxes;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮递员
 */
@Slf4j(topic = "c.Postman")
public class Postman extends Thread {
    /** 信箱id */
    private int id;
    /** 信件内容 */
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        log.debug("邮递员拿出{}号信件", id);
        GuardedObject guardedObject = Mailboxes.getGuardedObject(id);
        log.debug("写{}号信, 内容为{}", id, mail+id);
        guardedObject.complete(mail);
    }
}
