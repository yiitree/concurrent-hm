package cn.itcast.n8.同步集合;

import cn.itcast.n2.util.Sleeper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 单词计数案例 --- 多线程读取文件
 * 26个字母，每个200个，打乱保存在26个文件中，读取26个文件，记录每个字母的个数（每个字母200个）
 */
public class TestWordCount {
    public static void main(String[] args) {

        Object lock = new Object();

//        //创建 HashMap 当做保存容器
//        demo(
//                // 参数1：保存结果容器
//                () -> new HashMap<String,Integer>(),
//                // 参数2：单词计数
//                (map, words) -> {
//                    for (String word : words) {
//                        synchronized (lock){
//                            // 主要是get 、put 两个方法合起来不是安全的
//                            // 检查 key 有没有
//                            Integer counter = map.get(word);
//                            sleep(0.001);
//                            int newValue = counter == null ? 1 : counter + 1;
//                            // 没有 则 put
//                            map.put(word, newValue);
//                        }
//                    }
//                }
//        );

        // 使用ConcurrentHashMap
//        demo(
//                // 参数1：保存结果容器
//                // 创建 ConcurrentHashMap 对不对？
//                () -> new ConcurrentHashMap<String, LongAdder>(8,0.75f,8),
//                //参数2：保存方法
//                // 单词计数
//                (map, words) -> {
//                    for (String word : words) {
//
//                        // 每个操作是安全的，但是合并并不是安全的
////                        Integer counter = map.get(word);
////                        Sleeper.sleep(0.001);
////                        int newValue = counter == null ? 1 : counter + 1;
////                        // 没有 则 put
////                        map.put(word, newValue);
//
//
//                        // computeIfAbsent 如果缺少一个 key，则计算生成一个 value , 然后将  key value 放入 map
//                        //  如不存在，返回方法的返回值并存入map中;
//                        //  如已存在，返回map中的value值
//                        LongAdder value = map.computeIfAbsent(word, (key) -> new LongAdder());
//                        // 执行累加
//                        // 获得value的操作是原子的，说明只有一个线程可以得到key对应的value，
//                        // 执行累加的时候，每个线程拿到的是对应key中的value，此时累加互补干扰
//                        value.increment();
//                        Sleeper.sleep(0.001);
//                    }
//                }
//        );

        // 使用ConcurrentHashMap
        demo(
                // 参数1：保存结果容器
                // 创建 ConcurrentHashMap 对不对？
                () -> new HashMap<String, LongAdder>(),
                //参数2：保存方法
                // 单词计数
                (map, words) -> {
                    for (String word : words) {

                        // 每个操作是安全的，但是合并并不是安全的
//                        Integer counter = map.get(word);
//                        Sleeper.sleep(0.001);
//                        int newValue = counter == null ? 1 : counter + 1;
//                        // 没有 则 put
//                        map.put(word, newValue);


                        // computeIfAbsent 如果缺少一个 key，则计算生成一个 value , 然后将  key value 放入 map
                        //  如不存在，返回方法的返回值并存入map中;
                        //  如已存在，返回map中的value值

//                        LongAdder value = map.computeIfAbsent(word, (key) -> new LongAdder());

                        LongAdder value;
//                        synchronized (lock){
                            value = map.get(word);
                            if(value == null){
                                map.put(word,new LongAdder());
                            }
//                        }
                        if(map.get(word) != null){
                            map.get(word).increment();
                        }
                        // 执行累加
                        // 获得value的操作是原子的，说明只有一个线程可以得到key对应的value，
                        // 执行累加的时候，每个线程拿到的是对应key中的value，此时累加互补干扰

                        Sleeper.sleep(0.001);
                    }
                }
        );
    }


    private static void demo2() {
        Map<String, Integer> collect = IntStream.range(1, 27).parallel()
                .mapToObj(idx -> readFromFile(idx))
                .flatMap(list -> list.stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(w -> 1)));
        System.out.println(collect);
    }

    /**
     * 开启26个线程，每个线程读取一个文件
     * @param supplier 保存结果的集合（使用map集合）
     * @param consumer 带两个参数的消费器，
     *                 参数1：保存结果的集合
     *                 参数2：每个线程读取到的结果，list
     * @param <V>
     */
    private static <V> void demo(Supplier<Map<String, V>> supplier, BiConsumer<Map<String, V>, List<String>> consumer) {
        // 生成保存结果的集合，使用map
        // key value
        // a   200
        // b   200
        Map<String, V> counterMap = supplier.get();
        // 生成26个线程
        List<Thread> ts = new ArrayList<>();
        for (int i = 1; i <= 26; i++) {
            int idx = i;
            Thread thread = new Thread(() -> {
                // 读取文件
                List<String> words = readFromFile(idx);
                // 把每个线程读取到的list保存到map中
                consumer.accept(counterMap, words);
            });
            ts.add(thread);
        }
        // 启动线程
        ts.forEach(t -> t.start());
        // 等待所有线程结束
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 打印结果
        System.out.println(counterMap);
    }

    public static List<String> readFromFile(int i) {
        ArrayList<String> words = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("tmp/" + i + ".txt")))) {
            while (true) {
                String word = in.readLine();
                if (word == null) {
                    break;
                }
                words.add(word);
            }
            return words;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
