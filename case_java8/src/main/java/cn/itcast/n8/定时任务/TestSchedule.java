package cn.itcast.n8.定时任务;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务
 */
public class TestSchedule {

    // 如何让每周四 18:00:00 定时执行任务？
    public static void main(String[] args) {
        //  获取当前时间
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        // 获取本周 周四 18:00:00
        // now.withHour()....是修改时间的意思
        LocalDateTime time = now.withHour(18).withMinute(0).withSecond(0).withNano(0).with(DayOfWeek.THURSDAY);

        // 如果 当前时间 > 本周周四，必须找到下周周四
        if(now.compareTo(time) > 0) {
            time = time.plusWeeks(1);
        }
        System.out.println(time);

        // initailDelay 代表当前时间和周四的时间差
        long initailDelay = Duration.between(now, time).toMillis();

        // period 一周的间隔时间
        long period = 1000 * 60 * 60 * 24 * 7;

        // 任务
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(() -> {
            System.out.println("running...");
        }, initailDelay, period, TimeUnit.MILLISECONDS);
    }
}
