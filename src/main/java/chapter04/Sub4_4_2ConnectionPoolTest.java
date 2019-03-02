package chapter04;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter04
 * @date 2019/2/27 20:53
 * @description God Bless, No Bug!
 */
public class Sub4_4_2ConnectionPoolTest {

    static Sub4_4_2ConnectionPool pool = new Sub4_4_2ConnectionPool(10);

    /**
     * CountDownLatch这个类能够使一个线程等待其他线程完成各自的工作后再执行
     */
    // 保证所有ConnectionRunner能够同时开始
    static CountDownLatch start = new CountDownLatch(1);
    // main线程将会等待所有ConnectionRunner结束后才能继续执行
    static CountDownLatch end;

    public static void main(String[] args) throws Exception {
        // 线程数量，可以线程数量进行观察
        int threadCount = 50;
        end = new CountDownLatch(threadCount);
        int count = 20; // 每个线程尝试获取连接的次数
        AtomicInteger got = new AtomicInteger();
        AtomicInteger notGot = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new ConnectionRunner(count, got, notGot), "ConnectionRunnerThread");
            thread.start();
        }
        start.countDown(); // 保证所有线程同时开始获取连接
        end.await(); // 等待所有线程执行完
        System.out.println("total invoke: " + (threadCount * count));
        System.out.println("got connection:  " + got);
        System.out.println("not got connection " + notGot);
    }

    static class ConnectionRunner implements Runnable {
        int count;
        AtomicInteger got;
        AtomicInteger notGot;

        public ConnectionRunner(int count, AtomicInteger got, AtomicInteger notGot) {
            this.count = count;
            this.got = got;
            this.notGot = notGot;
        }

        public void run() {
            try {
                start.await();
            } catch (Exception ex) {

            }
            while (count > 0) {
                try {
                    // 从线程池中获取连接，如果1000ms内无法获取到，将会返回null
                    // 分别统计连接获取的数量got和未获取到的数量notGot
                    Connection connection = pool.fetchConnection(1000);
                    if (connection != null) {
                        try {
                            connection.createStatement();
                            connection.commit();
                        } finally {
                            pool.releaseConnection(connection);
                            got.incrementAndGet();
                        }
                    } else {
                        notGot.incrementAndGet();
                    }
                } catch (Exception ex) {
                } finally {
                    count--;
                }
            }
            end.countDown();
        }
    }
}
