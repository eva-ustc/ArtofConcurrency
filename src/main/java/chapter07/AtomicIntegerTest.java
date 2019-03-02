package chapter07;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter07
 * @date 2019/3/1 20:47
 * @description God Bless, No Bug!
 */
public class AtomicIntegerTest {
    static AtomicInteger ai = new AtomicInteger(1);

    public static void main(String[] args) {
        System.out.println(ai.getAndIncrement()); // 返回修改之前的值
        System.out.println(ai.get());
    }
}
