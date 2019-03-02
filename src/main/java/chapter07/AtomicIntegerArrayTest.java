package chapter07;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter07
 * @date 2019/3/1 21:01
 * @description God Bless, No Bug!
 */
public class AtomicIntegerArrayTest {

    static int[] value = new int[]{1,2};
    static AtomicIntegerArray ai = new AtomicIntegerArray(value);
    public static void main(String[] args) {
        System.out.println(ai.addAndGet(0,1));
        System.out.println(ai.getAndSet(1, 3)); // 返回修改前的值

        // AtomicIntegerArray内部复制一份value数组,所以原数组的值不会改变
        System.out.println("ai[0]: "+ai.get(0));
        System.out.println("value[0]: "+value[0]);
    }
}
