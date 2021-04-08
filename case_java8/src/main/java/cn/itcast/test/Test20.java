package cn.itcast.test;

import cn.itcast.n2.util.Sleeper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * 保护暂停模式
 */
@Slf4j(topic = "c.Test20")
public class Test20 {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        Sleeper.sleep(1);
        for (Integer id : Mailboxes.getIds()) {
            new Postman(id, "内容" + id).start();
        }
    }
}

/**
 * 居民
 */
@Slf4j(topic = "c.People")
class People extends Thread{
    @Override
    public void run() {
        // 创建一个信件
        GuardedObject guardedObject = Mailboxes.createGuardedObject();
        log.debug("开始收信 id:{}", guardedObject.getId());
        // 等五秒拿信件
        Object mail = guardedObject.get(5000);
        log.debug("收到信 id:{}, 内容:{}", guardedObject.getId(), mail);
    }
}

/**
 * 邮递员
 */
@Slf4j(topic = "c.Postman")
class Postman extends Thread {
    private final int id;
    private final String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObject guardedObject = Mailboxes.getGuardedObject(id);
        log.debug("送信 id:{}, 内容:{}", id, mail);
        guardedObject.complete(mail);
    }
}

/**
 * 信箱
 * 进行解耦，把返回结果放在信箱中
 * 要注意线程安全问题，因为是多线程访问
 */
class Mailboxes {
    // 选择hashTable，线程安全
    private static final Map<Integer, GuardedObject> boxes = new Hashtable<>();

    private static int id = 1;

    // 产生唯一 id
    private static synchronized int generateId() {
        return id++;
    }

    //Hashtable是安全的，所以就不需要加synchronized
    public static GuardedObject getGuardedObject(int id) {
        return boxes.remove(id);
    }

    /**
     * 创建一个信件，并放到信箱中
     * @return
     */
    public static GuardedObject createGuardedObject() {
        GuardedObject go = new GuardedObject(generateId());
        boxes.put(go.getId(), go);
        return go;
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}

// 增加超时效果

/**
 * 信件
 */
@Data
class GuardedObject {

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
     * 获取结果
     * @param timeout 表示要等待多久 2000
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
     * 产生结果
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
