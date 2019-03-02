package chapter11;

import common.SleepUtils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter11
 * @date 2019/3/2 20:00
 * @description God Bless, No Bug!
 * <p>
 * 一般方法:使用synchronized和wait(),notify()方式
 */
public class SynchronizedProducerConsumerPattern {

    private static final int MAX_SIZE = 10;
    private Queue<Integer> queue = new LinkedList<>();
    CountDownLatch count = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        SynchronizedProducerConsumerPattern test = new SynchronizedProducerConsumerPattern();
        for (int i = 0; i < 10; i++) {
            new Thread(test.new Producer()).start();
        }
        for (int i = 0; i < 5; i++) {
            new Thread(test.new Consumer()).start();
        }
        test.count.await();
    }

    /**
     * 生产者
     */
    private volatile int prod = 0;

    class Producer implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == MAX_SIZE) {
                        System.out.println("【生产者】队列满了，等待消费...");
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 队列未满,可以继续生产,唤醒等待的消费者
                    queue.offer(prod);
                    System.out.println("【生产者】生产一个产品" + (prod++) + "，当前队列中产品数量：" + queue.size());
                    SleepUtils.second(1);
                    queue.notify();
                }

            }
        }
    }

    /**
     * 消费者
     */
    class Consumer implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (queue) {

                    while (queue.size() == 0) {
                        System.out.println("【消费者】队列为空，等待生产...");
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 对列不为空,可以消费,唤醒生产者
                    Integer poll = queue.poll();
                    System.out.println("【消费者】消费了1个产品" + poll + "，当前队列中产品数量：" + queue.size());
                    SleepUtils.second(1);
                    queue.notify();
                }
            }
        }
    }
}
