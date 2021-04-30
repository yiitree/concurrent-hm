package test;

import java.util.HashMap;

public class TestDeadLink {
    public static void main(String[] args) {
        // 测试 java 7 中哪些数字的 hash 结果相等
        System.out.println("长度为16时，桶下标为1的key");
        for (int i = 0; i < 64; i++) {
            if (hash(i) % 16 == 1) {
                System.out.println(i);
            }
        }
        System.out.println("长度为32时，桶下标为1的key");
        for (int i = 0; i < 64; i++) {
            if (hash(i) % 32 == 1) {
                System.out.println(i);
            }
        }

        // 1, 35, 16, 50 当大小为16时，它们在一个桶内
        final HashMap<Integer, String> map = new HashMap<>();
        // 放 12 个元素
        map.put(2, "s2");
        map.put(3, "s3");
        map.put(4, "s4");
        map.put(5, "s5");
        map.put(6, "s6");
        map.put(7, "s7");
        map.put(8, "s8");
        map.put(9, "s9");
        map.put(10, "s10");
        // 扩容钱1,35,16在一条链
        // 扩容后 1,35 还在一条链
        map.put(16, "s16");
        map.put(35, "s35");
        map.put(1, "s1");

        System.out.println("扩容前大小[main]:"+map.size());

        /*
         * 发生扩容：元素个数大于3/4个数的时候开始扩容，原来16长度的，最多存放12个元素，增加到13开始扩容
         * 多存放一个，放13的元素开始扩容
         * 使用两个线程增加第13个元素
         */
        new Thread() {
            @Override
            public void run() {
                // 放第 13 个元素, 发生扩容
                map.put(50, "s50t0");
                System.out.println("扩容后大小[Thread-0]:"+map.size());
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                // 放第 13 个元素, 发生扩容
                map.put(50, "s50t1");
                System.out.println("扩容后大小[Thread-1]:"+map.size());
            }
        }.start();
    }

    final static int hash(Object k) {
        int h = 0;
        if (0 != h && k instanceof String) {
            return sun.misc.Hashing.stringHash32((String) k);
        }
        h ^= k.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
}
