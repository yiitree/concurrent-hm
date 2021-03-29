package cn.itcast.n4;

import java.util.ArrayList;

public class TestThreadSafe {

    static final int THREAD_NUMBER = 2;
    static final int LOOP_NUMBER = 2000;

    public static void main(String[] args) {
        ThreadSafeSubClass2 test = new ThreadSafeSubClass2();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                test.method1(LOOP_NUMBER);
            }, "Thread" + (i + 1)).start();
        }
    }
}

/**
 * 不安全的
 */
class ThreadUnsafe {
    /** 此时为成员变量 */
    ArrayList<String> list = new ArrayList<>();
    public void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
            method2();
            method3();
        }
    }
    private void method2() {
        list.add("1");
    }
    private void method3() {
        list.remove(0);
    }
}

/**
 * 安全的
 */
class ThreadSafe {
    public final void method1(int loopNumber) {
        // 此时为局部变量
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }
    }
    public void method2(ArrayList<String> list) {
        list.add("1");
    }
    public void method3(ArrayList<String> list) {
        System.out.println(1);
        list.remove(0);
    }
}

/**
 * 不安全的
 */
class ThreadSafeSubClass2 {
    public final void method1(int loopNumber) {
        // 此时为局部变量
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }
    }
    public void method2(ArrayList<String> list) {
        list.add("1");
    }

    /**
     * 相当于完全交给一个线程去操作这个对象，于是就有两个线程操作同一个对象了，就回出现安全问题
     * @param list
     */
    public void method3(ArrayList<String> list) {
        System.out.println(3);
        new Thread(() -> {
            list.remove(0);
        }).start();
    }
}

/**
 * 此时使用子类重写操作局部变量方法
 */
class ThreadSafeSubClass extends ThreadSafe {
    @Override
    public void method3(ArrayList<String> list) {
        System.out.println(2);
        new Thread(() -> {
            list.remove(0);
        }).start();
    }
}
