package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicStampedReference;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * AtomicStampedReference
 * ABA问题：---增加版本号
 * 就是其他线程改了两次，a改为b又改为a，此时主线程得到一看没有变又继续改了
 */
@Slf4j(topic = "c.Test36")
public class Test36 {

    /**
     * 原子引用，初始值为A
     * 增加版本号，初始为0
     */
    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 0);

    /**
     * 提供一个主线程，想要吧A改为C
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
        // 获取值 A
        String prev = ref.getReference();
        // 获取版本号
        int stamp = ref.getStamp();
        log.debug("版本 {}", stamp);
        // 如果中间有其它线程干扰，发生了 ABA 现象
        other();
        sleep(1);
        // 尝试改为 C
        log.debug("change A->C {}", ref.compareAndSet(prev, "C", stamp, stamp + 1));
    }

    /**
     * 其他线程
     */
    private static void other() {
        new Thread(() -> {
            log.debug("change A->B {}", ref.compareAndSet(ref.getReference(), "B", ref.getStamp(), ref.getStamp() + 1));
            log.debug("更新版本为 {}", ref.getStamp());
        }, "t1").start();
        sleep(0.5);
        new Thread(() -> {
            log.debug("change B->A {}", ref.compareAndSet(ref.getReference(), "A", ref.getStamp(), ref.getStamp() + 1));
            log.debug("更新版本为 {}", ref.getStamp());
        }, "t2").start();
    }
}
