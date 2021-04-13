package cn.itcast.test;

import lombok.Data;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Unsafe---这里指的是太过于底层，不建议开发人员调用，所以叫做unsafe
 * 只能通过反射获取
 */
public class TestUnsafe {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InstantiationException {
//        Unsafe unsafe = (Unsafe)Class.forName("sun.misc.Unsafe").newInstance();
        // 获得Unsafe类的theUnsafe字段
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        // 设置允许访问私有变量
        theUnsafe.setAccessible(true);
        // 获得成员变量，由于是静态变量，所以不需要任何传参
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        System.out.println(unsafe);

        // 下面是使用unsafe方式线程安全的方式对对象进行修改
        // 可以更高级的使用AtomicReference封装好的进行修改

        // 1. 获取域的偏移地址---其实就是指明是哪个类的哪个字段
        long idOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("id"));
        long nameOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("name"));

        // 2. 执行 cas 操作
        Teacher t = new Teacher();
        // 比较并交换整型 （对象，偏移量地址，修改前的值，要修改后的值）
        unsafe.compareAndSwapInt(t, idOffset, 0, 1);
        unsafe.compareAndSwapObject(t, nameOffset, null, "张三");

        // 3. 验证
        System.out.println(t);
    }
}
@Data
class Teacher {
    volatile int id;
    volatile String name;
}


