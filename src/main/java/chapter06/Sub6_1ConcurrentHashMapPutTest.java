package chapter06;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter06
 * @date 2019/3/1 14:50
 * @description God Bless, No Bug!
 */
public class Sub6_1ConcurrentHashMapPutTest {
    public static void main(String[] args) throws InterruptedException {
//        ConcurrentHashMap hashMap = new ConcurrentHashMap();
//        ConcurrentLinkedQueue queue;
        BlockingQueue blockingQueue;
        final HashMap<String, String> map = new HashMap<String, String>(2);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            map.put(UUID.randomUUID().toString(), "");
                        }
                    }, "ftf" + i).start();
                }
            }
        }, "ftf");
        t.start();
        t.join();
    }
}
