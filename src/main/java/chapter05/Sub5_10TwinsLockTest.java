package chapter05;

import common.SleepUtils;

import java.util.concurrent.locks.Lock;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter05
 * @date 2019/3/1 11:23
 * @description God Bless, No Bug!
 */
public class Sub5_10TwinsLockTest {
    static final Lock lock = new Sub5_10TwinsLock();
    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Worker());
            thread.start();
        }
        for (int i = 0; i < 10; i++) {
            SleepUtils.second(1);
            System.out.println();
        }
    }
    static class Worker implements Runnable {
        @Override
        public void run() {
            while (true){
                lock.lock();
                try {
                    SleepUtils.second(1);
                    System.out.println(Thread.currentThread().getName());
                    SleepUtils.second(1);
                }finally {
                    lock.unlock();
                }
            }
        }
    }
}
