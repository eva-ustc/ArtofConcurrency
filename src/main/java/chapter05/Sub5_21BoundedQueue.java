package chapter05;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter05
 * @date 2019/3/1 12:46
 * @description God Bless, No Bug!
 */
public class Sub5_21BoundedQueue<T> {

    private Object[] items;
    private Lock lock = new ReentrantLock();
    Condition notFull = lock.newCondition();
    Condition notEmpty = lock.newCondition();
    private int addIndex, removeIndex, count;

    Sub5_21BoundedQueue(int size) {
        items = new Object[size];
    }

    public void add(T t) throws InterruptedException {

        lock.lock();
        try {

            while (count >= items.length) {
                notFull.await(); // 如果数组已满,则等待,在remove()方法调用之后唤醒
            }
            items[addIndex] = t;
            if (++addIndex == items.length) {
                addIndex = 0;
            }
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T remove() throws InterruptedException {
        lock.lock();
        try {

            while (count <= 0) {
                notEmpty.await();
            }
            Object item = items[removeIndex];
            if (++removeIndex == items.length) {
                removeIndex = 0;
            }
            --count;
            notFull.signal();
            return (T) item;
        } finally {
            lock.unlock();
        }

    }
}
