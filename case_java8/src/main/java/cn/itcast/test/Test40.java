package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 字段更新器
 */
@Slf4j(topic = "c.Test40")
public class Test40 {

    public static void main(String[] args) {

        Student stu = new Student();

        // 多个属性针对student对象的name属性进行修改
        // 是直接进行属性修改，而不是调用get/set方法
        AtomicReferenceFieldUpdater updater =
                AtomicReferenceFieldUpdater.newUpdater(
                        // 修改字段所属对象的类型
                        Student.class,
                        // 修改字段的类型
                        String.class,
                        // 修改字段的名称
                    "name");
        System.out.println(updater.compareAndSet(stu, null, "张三"));
        System.out.println(stu);
    }
}


class Student {
    // 注意要修改的类型必须要volatile
    volatile String name;
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
