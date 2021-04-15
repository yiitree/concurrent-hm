//package cn.itcast.n8.自定义线程池.my;
//
///**
// * @Author: 曾睿
// * @Date: 2021/4/15 10:53
// */
//public class Worker extends Thread{
//
//
//    /**
//     * 任务
//     * 就是一个个等待start的线程
//     * 线程池的任务就是调用worker的start()方法，
//     * 所以线程池就是控制同时可以启动任务的个数
//     */
//
//        // 任务对象
//        private Runnable task;
//
//        public Worker(Runnable task) {
//            this.task = task;
//        }
//
//        /**
//         * 执行任务
//         * 重写run方法
//         */
//        @Override
//        public void run() {
//            // 1) 当 task 不为空，执行任务
//            // 2) 当 task 执行完毕，再接着从任务队列获取任务并执行
////            while(task != null || (task = taskQueue.take()) != null) {
//            while(task != null || (task = taskQueue.poll(timeout, timeUnit)) != null) {
//                try {
//                    log.debug("正在执行...{}", task);
//                    // 开启任务
//                    task.run();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    task = null;
//                }
//            }
//            // 执行完了，就把workers移除掉
//            synchronized (workers) {
//                log.debug("worker 被移除{}", this);
//                workers.remove(this);
//            }
//        }
//
//}
