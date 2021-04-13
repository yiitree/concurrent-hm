package cn.itcast.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 原子累加器，LongAdder
 * 就是i++这种，相比AtomicInteger.getAndIncrement()效率更高
 */
public class Test41 {

    public static void main(String[] args) {

        final LongAdder longAdder = new LongAdder();
        System.out.println(longAdder);
        longAdder.increment();
        final long l = longAdder.sum();
        System.out.println(l);

        for (int i = 0; i < 5; i++) {
            demo(
                // 使用自增构造getAndIncrement
                () -> new AtomicLong(0),
                (adder) -> adder.getAndIncrement()
            );
        }
        for (int i = 0; i < 5; i++) {
            demo(
                // 使用LongAdder
                () -> new LongAdder(),
                adder -> adder.increment()
            );
        }
    }

    /**
     *
     * @param adderSupplier () -> 结果    提供累加器对象
     * @param action (参数) ->     执行累加操作
     * @param <T>
     */
    private static <T> void demo(Supplier<T> adderSupplier,
                                 Consumer<T> action) {
        T adder = adderSupplier.get();
        List<Thread> ts = new ArrayList<>();
        // 16 个线程，每人累加 50 万
        for (int i = 0; i < 10; i++) {
            ts.add(new Thread(() -> {
                for (int j = 0; j < 500000; j++) {
                    action.accept(adder);
                }
            }));
        }
        long start = System.nanoTime();
        ts.forEach(t -> t.start());
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        long end = System.nanoTime();
        System.out.println(adder + " cost:" + (end - start) / 1000_000);
    }
}
