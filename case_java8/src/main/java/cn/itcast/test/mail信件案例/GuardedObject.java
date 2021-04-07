package cn.itcast.test.mail信件案例;

import lombok.Data;

// 增加超时效果

/**
 * 信件
 */
@Data
public class GuardedObject {

    /**
     * 标识 Guarded Object
     * 相当于信件id，每一个信件都需要一个编号
     */
    private int id;

    /**
     * 结果
     * 相当于信件内容
     */
    private Object response;

    public GuardedObject(int id) {
        this.id = id;
    }

    /**
     * 获取信件
     * @param timeout 超时时间 表示要等待多久 2000
     * @return
     */
    public Object get(long timeout) {
        synchronized (this) {
            // 开始时间 15:00:00
            long begin = System.currentTimeMillis();
            // 经历的时间
            long passedTime = 0;
            while (response == null) {
                // 这一轮循环应该等待的时间，超时时间-等待时间
                long waitTime = timeout - passedTime;
                // 经历的时间超过了最大等待时间时，退出循环
                if (timeout - passedTime <= 0) {
                    break;
                }
                try {
                    // 虚假唤醒 15:00:01
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 求得经历时间  15:00:02  1s
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    /**
     * 写信
     * @param response
     */
    public void complete(Object response) {
        synchronized (this) {
            // 给结果成员变量赋值
            this.response = response;
            this.notifyAll();
        }
    }
}
