package chapter11;

import common.SleepUtils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter11
 * @date 2019/3/2 21:05
 * @description God Bless, No Bug!
 */
public class BlockQueueProducerConsumerPattern {
    static CountDownLatch count = new CountDownLatch(1);
    static BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(5);
    static final int MAX_SIZE = 10;
    static volatile int goods = 0;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new Thread(new Producer(),"Producer"+i).start();
        }
        for (int i = 0; i < 5; i++) {
            new Thread(new Consumer(),"Consumer"+i).start();
        }
        count.await();
    }

    private static class Producer implements Runnable {
        @Override
        public void run() {
            while (true) {

                /*while (queue.size() == MAX_SIZE) {
                    System.out.println("【生产者】队列满了，等待消费...");
                }*/
                SleepUtils.second(1);
                try {
                    queue.put(goods);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("【生产者"+Thread.currentThread().getName()+"】生产了1个产品" + (goods++) + "，当前队列中产品数量：" + queue.size());
            }
        }
    }

    private static class Consumer implements Runnable {
        @Override
        public void run() {
            while (true){

                /*while (queue.size()==0){
                    System.out.println("【消费者】队列为空，等待生产...");
                }*/
                SleepUtils.second(1);
                Integer poll = null;
                try {
                    poll = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("【消费者"+Thread.currentThread().getName()+"】消费了1个产品" + poll + "，当前队列中产品数量：" + queue.size());
            }
        }
    }
}
