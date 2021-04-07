package cn.itcast.test.my;

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

        // 居民先把空白信放进信箱
        for (int i = 0; i < 3; i++) {
            final People people = new People();
            people.start();
        }

        Sleeper.sleep(1);

        for (int i = 0; i < 3; i++) {
            final Postman postman = new Postman(i, "信内容：");
            postman.start();
        }
    }
}
