package cn.itcast.test.my;

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
        MyMail myMail = MyMailbox.getMail(id);
        log.debug("邮递员拿出{}号信件", id);
        try {
            myMail.setContent(mail+"信箱编号为："+id);
            log.debug("写好{}号信, 内容为{}", id, myMail.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
