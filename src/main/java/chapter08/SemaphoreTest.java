package chapter08;

import common.SleepUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    private static final int THREAD_COUNT = 30;

    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);

    private static Semaphore s = new Semaphore(10);

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        s.acquire();
                        System.out.println("等待线程数: "+s.getQueueLength());
                        System.out.println("许可证可用数: " +s.availablePermits());
                        System.out.println();
                        SleepUtils.second(2);
                        s.release();
                    } catch (InterruptedException e) {
                    }
                }
            });
        }

        threadPool.shutdown();
    }
}