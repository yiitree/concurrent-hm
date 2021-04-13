package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicMarkableReference;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * 只是想要只是否有别人进行修改
 * AtomicMarkableReference---只需要一个boolean值即可
 * 相当于一个简化版本
 */
@Slf4j(topic = "c.Test38")
public class Test38 {
    public static void main(String[] args) {

        GarbageBag bag = new GarbageBag("装满了垃圾");
        // 参数2 mark 可以看作一个标记，表示垃圾袋满了
        AtomicMarkableReference<GarbageBag> ref = new AtomicMarkableReference<>(bag, true);

        log.debug("start...");
        GarbageBag prev = ref.getReference();
        log.debug(prev.toString());

        // 保洁阿姨到垃圾
        new Thread(() -> {
            log.debug("start...");
            bag.setDesc("空垃圾袋");
            // 如果标记为true，则倒垃圾，然后把标记改为false
            ref.compareAndSet(bag, bag, true, false);
            log.debug(bag.toString());
        },"保洁阿姨").start();

        sleep(1);
        log.debug("想换一只新垃圾袋？");
        // 如果标记为true，则换一个新垃圾袋，然后把标记改为false
        boolean success = ref.compareAndSet(prev, new GarbageBag("空垃圾袋"), true, false);
        log.debug("换了么？" + success);
        log.debug(ref.getReference().toString());
    }
}

class GarbageBag {
    String desc;

    public GarbageBag(String desc) {
        this.desc = desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    @Override
    public String toString() {
        return super.toString() + " " + desc;
    }
}
