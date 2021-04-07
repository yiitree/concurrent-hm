package cn.itcast.test.mail信件案例;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * 信箱，为一个hashTable
 * 进行解耦，把返回结果放在信箱中
 * 要注意线程安全问题，因为是多线程访问
 */
public class Mailboxes {

    /** 选择hashTable，线程安全 */
    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();
    /** 信箱编号 */
    private static int id = 1;

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
    public static GuardedObject getGuardedObject(int id) {
        return boxes.remove(id);
    }

    /**
     * 创建一个信件，并放到信箱中，此时信件中没有内容，只有id
     * @return 信件
     */
    public static synchronized GuardedObject createGuardedObject() {
        GuardedObject go = new GuardedObject(id++);
        // 放入信件
        boxes.put(go.getId(), go);
        return go;
    }

}
