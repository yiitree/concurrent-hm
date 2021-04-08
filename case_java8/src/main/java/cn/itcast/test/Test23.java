package cn.itcast.test;

import cn.itcast.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用ReentrantLock锁处理哲学家吃饭问题
 */
@Slf4j(topic = "c.Test23")
public class Test23 {public static void main(String[] args) {
    Chopstick c1 = new Chopstick("1");
    Chopstick c2 = new Chopstick("2");
    Chopstick c3 = new Chopstick("3");
    Chopstick c4 = new Chopstick("4");
    Chopstick c5 = new Chopstick("5");
    new Philosopher("苏格拉底", c1, c2).start();
    new Philosopher("柏拉图", c2, c3).start();
    new Philosopher("亚里士多德", c3, c4).start();
    new Philosopher("赫拉克利特", c4, c5).start();
    new Philosopher("阿基米德", c5, c1).start();
}
}

@Slf4j(topic = "c.Philosopher")
class Philosopher extends Thread {
    Chopstick left;
    Chopstick right;

    public Philosopher(String name, Chopstick left, Chopstick right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    /**
     * 没有获得筷子的时候就放弃这次获取
     */
    @Override
    public void run() {
        while (true) {
            //　尝试获得左手筷子
            if(left.tryLock()) {
                try {
                    // 尝试获得右手筷子
                    if(right.tryLock()) {
                        try {
                            eat();
                        } finally {
                            // 放回右手筷子
                            right.unlock();
                        }
                    }
                } finally {
                    // 如果没有拿到右手筷子，就放回左手筷子
                    left.unlock();
                }
            }
        }
    }

    Random random = new Random();
    private void eat() {
        log.debug("eating...");
        Sleeper.sleep(0.5);
    }
}

class Chopstick extends ReentrantLock {
    String name;

    public Chopstick(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子{" + name + '}';
    }
}
