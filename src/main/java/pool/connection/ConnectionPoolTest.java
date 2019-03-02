package pool.connection;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name pool
 * @date 2019/2/27 21:29
 * @description God Bless, No Bug!
 *
 *  耗费资源的对象使用池技术,包含超时功能
 *      测试 启动50个线程,每个线程尝试获取连接20次,统计正常获取和超时的次数
 */
public class ConnectionPoolTest {

    static ConnectionPool pool = new ConnectionPool(10); // 创建有10个连接的连接池

    static CountDownLatch start = new CountDownLatch(1); //让所有线程同时开始执行
    static CountDownLatch end; // 主线程等待所有线程执行完
    public static void main(String[] args) throws InterruptedException {

        int threadCount = 50;
        int count = 20;
        end = new CountDownLatch(threadCount);
        AtomicInteger got = new AtomicInteger(); // 未超时的线程数
        AtomicInteger notGot = new AtomicInteger(); // 超时的线程数
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new ConnectionRunner(count,got,notGot));
            thread.start();
        }
        start.countDown();
        end.await();
        // 输出结果
        System.out.println("total invoke: " + (threadCount * count));
        System.out.println("got connection:  " + got);
        System.out.println("not got connection " + notGot);

    }
    static class ConnectionRunner implements Runnable{
        AtomicInteger got;
        AtomicInteger notGot;
        int count;
        @Override
        public void run() {
            try {
                start.await(); // 让所有线程同时开始执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 尝试获取连接20次
            while (count>0){
                try {
                    Connection connection = pool.getConnection(1000);
                    if (connection!=null){
                        try {
                            connection.createStatement();
                            connection.commit();
                        }finally {
                            got.incrementAndGet();
                            pool.releaseConnection(connection);
                        }
                    }else {
                        notGot.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    count--;
                }
            }
            end.countDown();
        }

        public ConnectionRunner(int count, AtomicInteger got, AtomicInteger notGot){
            this.got = got;
            this.notGot = notGot;
            this.count = count;
        }
    }
}
