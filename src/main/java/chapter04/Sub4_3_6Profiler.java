package chapter04;

import java.util.concurrent.TimeUnit;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter04
 * @date 2019/2/26 23:13
 * @description God Bless, No Bug!
 *
 * ThreadLocal类中可重载的方法只有四个：
 *
 *       1）set()：设置值，也就是说，我们选择将某个值设置为ThreadLocal类型的；
 *
 *       2）get()：将设置进去的值取出来；
 *
 *       3）remove()：我们不想将某个值设置为ThreadLocal了，移除掉；
 *
 *       4）initialValue()：如果get的时候还没有设置值，就使用这个方法进行初始化；
 *
 *  使用过程简单明了，一般重载initialValue()提供一个初始值就可以了，其余方法不需要重载。
 */
public class Sub4_3_6Profiler {
    // 第一次get()方法调用时会进行初始化（如果set方法没有调用），每个线程会调用一次
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };

    public static final void begin() {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    public static final long end() {
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }

    public static void main(String[] args) throws Exception {
        Sub4_3_6Profiler.begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Cost: " + Sub4_3_6Profiler.end() + " mills");
    }
}
