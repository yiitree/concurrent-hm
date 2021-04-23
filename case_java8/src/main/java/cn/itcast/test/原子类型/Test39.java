package cn.itcast.test.原子类型;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 原子数组
 * 对数组里的元素进行操作，
 * 10个元素，然后new10个线程，
 * 每个线程分别对数组进行顺序操作，每个元素+1,直到自增到10000
 */
public class Test39 {

    public static void main(String[] args) {
        demo(
                () -> new int[5],
                (array) -> array.length,
                (array, index) -> array[index]++,
                (array) -> System.out.println(Arrays.toString(array))
        );

        demo(
                () -> new AtomicIntegerArray(5),
                (array) -> array.length(),
                (array, index) -> array.getAndIncrement(index),
                (array) -> System.out.println(array)
        );
    }

    /**
     * supplier 提供者：  没有参数 有结果：()->结果
     * function 函数：   一个参数 有结果：(参数)->结果
     * BiFunction：   多个参数 有结果: (参数1,参数2)->结果
     * consumer 消费者： 一个参数 没结果：(参数)->void
     * BiConsumer：   多个参数 没结果：(参数1,参数2)->void
     *
     * @param arraySupplier 提供数组、可以是线程不安全数组或线程安全数组
     * @param lengthFun     获取数组长度的方法
     * @param putConsumer   自增方法，回传 array, index
     * @param printConsumer 打印数组的方法
     * @param <T>
     */
    private static <T> void demo(
            Supplier<T> arraySupplier,
            Function<T, Integer> lengthFun,
            BiConsumer<T, Integer> putConsumer,
            Consumer<T> printConsumer
    ) {
        // 获得数组
        T array = arraySupplier.get();
        // 获取数组长度
        int length = lengthFun.apply(array);
        // 开启一个线程数组，线程个数等于数组个数
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            // 每个线程对数组作 10000 次操作
            ts.add(new Thread(() -> {
                // 每个线程都增加10000次，一共length个线程，但是有length长度的数据，
                // 所以每个元素为：10000*length/length
                for (int j = 0; j < 10000; j++) {
                    // 对(j%length)的元素进行自增到10000
                    putConsumer.accept(array, j % length);
                }
            }));
        }
        // 启动所有线程
        ts.forEach(t -> t.start());
        // 等所有线程结束
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 打印数组
        printConsumer.accept(array);
    }
}
