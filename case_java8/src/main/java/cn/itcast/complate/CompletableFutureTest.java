package cn.itcast.complate;

import org.junit.Test;

import java.util.concurrent.*;

public class CompletableFutureTest {

    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    /**
     * 轮询异步结果并获取
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testFutureAsk() throws ExecutionException, InterruptedException {

        // 任务1
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Runnable异步线程开始...");
                    TimeUnit.SECONDS.sleep(4);
                    System.out.println("Runnable异步线程结束...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // 任务2
        final Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("Callable异步线程开始...");
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Callable异步线程结束...");
                return "callableResult";
            }
        };

        // 开启两个任务
        Future<String> runnableFuture = executor.submit(runnable, "fakeRunnableResult");
        Future<String> callableFuture = executor.submit(callable);

        System.out.println("Runnable执行结果:" + runnableFuture.get());
        System.out.println("Callable执行结果:" + callableFuture.get());

//        boolean runnableDone = false;
//        boolean callableDone = false;
//
//        // 不断轮询，直到所有任务结束
//        while (true) {
//            TimeUnit.MILLISECONDS.sleep(500);
//            System.out.println("轮询异步结果...");
//            if (runnableFuture.isDone()) {
//                System.out.println("Runnable执行结果:" + runnableFuture.get());
//                runnableDone = true;
//            }
//            if (callableFuture.isDone()) {
//                System.out.println("Callable执行结果:" + callableFuture.get());
//                callableDone = true;
//            }
//            if (runnableDone && callableDone) {
//                break;
//            }
//        }

        System.out.println("任务全部结束");
    }
}
