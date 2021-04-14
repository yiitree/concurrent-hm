package cn.itcast.n5;

/**
 * 变量获取方式
 * @Author: 曾睿
 * @Date: 2021/4/14 15:44
 */
public class Demo {

    // getfield
    int a = 10;
    // getstatic，走的是堆空间中
    static int a1 = 10;
    // getstatic
    static int a2 = Short.MAX_VALUE+1;
    // 栈内存都会初始一些值，要是小就直接从栈内存中复制一份变量，走的是栈，线程内部，比较快
    // 加了final的变量走的为 bipush，是复制一份变量，放到栈内存
    final int a3 = 10;
    // ldc，走常量池,类比较大，就放到常量池中
    final int a4 = Short.MAX_VALUE+1;

    public void get(){
        int b = this.a;
        int b1 = Demo.a1;
        int b2 = Demo.a2;
        Demo demo = new Demo();
        int b3 = demo.a3;
        int b4 = demo.a4;
        int b5 = demo.a;
    }
}
