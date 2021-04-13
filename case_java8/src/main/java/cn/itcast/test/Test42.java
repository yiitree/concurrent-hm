package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 使用unsafe自定义创建一个线程安全的对象
 */
@Slf4j(topic = "c.Test42")
public class Test42 {
    public static void main(String[] args) {
        Account.demo(new MyAtomicInteger(10000));
    }
}

class MyAtomicInteger implements Account {
    private final int value;
    private static final long valueOffset;
    private static final Unsafe UNSAFE;
    static {
        // 获得一个Unsafe对象
        UNSAFE = UnsafeAccessor.getUnsafe();
        try {
            valueOffset = UNSAFE.objectFieldOffset(MyAtomicInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int getValue() {
        return value;
    }

    public void decrement(int amount) {
        while(true) {
            // 修改之前的钱
            int prev = this.value;
            // 修改之后的钱
            int next = prev - amount;
            // 进行减钱操作，如果失败就break
            if (UNSAFE.compareAndSwapInt(this, valueOffset, prev, next)) {
                break;
            }
        }
    }

    public MyAtomicInteger(int value) {
        this.value = value;
    }

    @Override
    public Integer getBalance() {
        return getValue();
    }

    @Override
    public void withdraw(Integer amount) {
        decrement(amount);
    }
}


/**
 * 自定义的小工具类，用于获得Unsafe对象的
 */
class UnsafeAccessor {
    private static final Unsafe unsafe;
    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error(e);
        }
    }
    public static Unsafe getUnsafe() {
        return unsafe;
    }
}
