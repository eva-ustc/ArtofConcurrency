package chapter04;

import common.SleepUtils;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter04
 * @date 2019/2/26 19:44
 * @description God Bless, No Bug!
 */
public class Sub4_1_5Daemon {
    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonRunner(),"DaemonRunner");
        thread.setDaemon(true);
        thread.start();
    }

    static class DaemonRunner implements Runnable{

        @Override
        public void run() {
            try {

                SleepUtils.second(10);
            }finally {
                System.out.println("DaemonThread finally run...");
            }
        }
    }
}
