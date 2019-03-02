package chapter04;

import common.SleepUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter04
 * @date 2019/2/26 20:17
 * @description God Bless, No Bug!
 */
public class Sub4_2_3Interrupted {
    public static void main(String[] args) throws Exception {
        // sleepThread不停的尝试睡眠
        Thread sleepThread = new Thread(new SleepRunner(), "SleepThread");
        sleepThread.setDaemon(true);
        // busyThread不停的运行
        Thread busyThread = new Thread(new BusyRunner(), "BusyThread");
        busyThread.setDaemon(true);
        sleepThread.start();
        busyThread.start();
        // 休眠5秒，让sleepThread和busyThread充分运行
        TimeUnit.SECONDS.sleep(5);
        sleepThread.interrupt();
        busyThread.interrupt();

        // false 中断标识位被复位为false
        System.out.println("SleepThread interrupted is " + sleepThread.isInterrupted());
        // true 工作线程中断标识位正常设为true
        System.out.println("BusyThread interrupted is " + busyThread.isInterrupted());

        // 防止sleepThread和busyThread立刻退出
        TimeUnit.SECONDS.sleep(2);
    }

    static class SleepRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
                SleepUtils.second(10);
            }
        }
    }

    static class BusyRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
            }
        }
    }
}
