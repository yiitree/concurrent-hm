package cn.itcast.test.my;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * 信箱，为一个hashTable
 * 进行解耦，把返回结果放在信箱中
 * 要注意线程安全问题，因为是多线程访问
 */
public class MyMailbox {

    /** 选择hashTable，线程安全 */
    private static Map<Integer, MyMail> boxes = new Hashtable<>();
    /** 信箱编号 */
    private static int id = 0;

    /**
     * 得到所有信件的id
     * @return id编号
     */
    public static Set<Integer> getIds() {
        return boxes.keySet();
    }

    /**
     * 得到一个信件
     * Hashtable是安全的，所以就不需要加synchronized
     * @param id 信件id
     * @return 信件
     */
    public static MyMail getMail(int id) {
        return boxes.remove(id);
    }

    /**
     * 创建一个信件，并放到信箱中，此时信件中没有内容，返回信箱id
     * @return 信件
     */
    public static synchronized MyMail putMail() {
        MyMail myMail = new MyMail();
        // 放入信件
        boxes.put(id++, myMail);
        return myMail;
    }

}
