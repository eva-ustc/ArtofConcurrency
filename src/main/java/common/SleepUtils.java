package common;

import java.util.concurrent.TimeUnit;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter04
 * @date 2019/2/26 19:32
 * @description God Bless, No Bug!
 */
public class SleepUtils {
    public static final void second(long second){
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
