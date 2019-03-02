package chapter03;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter03
 * @date 2019/2/25 21:04
 * @description God Bless, No Bug!
 */
public class Sub3_5_3ReentrantLockExample {
    public static void main(String[] args) {
        Sub3_5_3ReentrantLockExample test = new Sub3_5_3ReentrantLockExample();
        test.writer();
    }
    int a = 0;
    ReentrantLock lock = new ReentrantLock();

    public void writer() {
        lock.lock(); //获取锁
        try {
            a++;
        } finally {
            lock.unlock(); //释放锁
        }
    }

    public void reader() {
        lock.lock(); //获取锁
        try {
            int i = a;
            // ...
        } finally {
            lock.unlock(); //释放锁
        }
    }

}
