package cn.itcast.test.mail信件案例;

import cn.itcast.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

/**
 * 保护暂停模式
 * 步骤：
 * 第一步：居民创建信件，得到信件编号（信件无内容）
 * 第二步：邮递员拿出信件，并写信件内容（写内容）
 * 第三步：居民拿出信件并读信件内容（有内容）
 */
@Slf4j(topic = "c.Test20")
public class Test {
    public static void main(String[] args){
        // 三个居民等待收信
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        // 间隔一秒后开始送信
        Sleeper.sleep(1);
        // 遍历有多少信件，每一个信都让一个邮递员送信
        for (Integer id : Mailboxes.getIds()) {
            new Postman(id, "信件内容为：" + id).start();
        }
    }
}
