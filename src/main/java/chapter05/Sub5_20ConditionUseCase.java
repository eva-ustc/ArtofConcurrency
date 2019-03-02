package chapter05;

import common.SleepUtils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter05
 * @date 2019/3/1 12:28
 * @description God Bless, No Bug!
 */
public class Sub5_20ConditionUseCase {
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Sub5_20ConditionUseCase test = new Sub5_20ConditionUseCase();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    test.testAwait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                test.testSignal();
            }
        }).start();
    }

    public void testAwait() throws InterruptedException {
        lock.lock();
        try {
            System.out.println("等待开始...");
            condition.await();
            System.out.println("等待结束...");
        }finally {
            lock.unlock();
        }
    }
    public void testSignal(){
        lock.lock();
        try {
            System.out.println("2秒后唤醒...");
            SleepUtils.second(2);
            condition.signal();
        }finally {
            lock.unlock();
        }
    }
}
