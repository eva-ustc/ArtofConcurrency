package pool.thread;

import common.SleepUtils;

import java.util.concurrent.CountDownLatch;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name pool.thread
 * @date 2019/2/27 23:38
 * @description God Bless, No Bug!
 */
public class ThreadPoolTest {
    public static void main(String[] args) {
        DefaultThreadPool<ThreadRunner> pool = new DefaultThreadPool<>();
        for (int i = 0; i < 10; i++) {
            ThreadRunner runner = new ThreadRunner();
            pool.execute(runner); // execute()方法将runner加入到jobs队列
        }
        while (pool.getJobSize()!=0){
            SleepUtils.second(1);
        }
        pool.shutdown();
    }
    static volatile int count = 0;
    static class ThreadRunner implements Runnable{
        @Override
        public void run() {
            System.out.println("ThreadPool execute job..."+count++);
            SleepUtils.second(3);
        }
    }
}
