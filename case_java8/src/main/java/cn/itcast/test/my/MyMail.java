package cn.itcast.test.my;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * 信件
 */
@Data
public class MyMail {

    private String content;

    private final Object lock = new Object();
    /**
     * 写信
     */
    public void setContent(String content) throws InterruptedException {
        synchronized (this){
            // 写完信，唤醒读线程
            TimeUnit.SECONDS.sleep(2);
            this.content = content;
            this.notifyAll();
        }
    }

    /**
     * 读信---必须先写信才可以否则返回null
     */
    public String get() throws InterruptedException {
        synchronized (this){
            while (content == null){
                this.wait();
            }
            TimeUnit.SECONDS.sleep(2);
            return this.content;
        }
    }
}
