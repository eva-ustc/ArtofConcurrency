package chapter05;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter05
 * @date 2019/3/1 11:04
 * @description God Bless, No Bug!
 */
public class Sub5_10TwinsLock implements Lock {

    private final Sync sync = new Sync(2);
    private static class Sync extends AbstractQueuedSynchronizer{

        Sync(int count){
            if (count<0){
                throw new IllegalArgumentException("count must larger than zero!");
            }
            setState(count);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            for (;;){
                int current = getState();
                int newCount = current-arg;
                if (newCount<0 || compareAndSetState(current,newCount)){
                    return newCount;
                }
            }
        }

        @Override
        public boolean tryReleaseShared(int arg) {
            for (;;){
                int current = getState();
                int newCount = current+arg;
                if (compareAndSetState(current,newCount)){
                    return true;
                }
            }
        }
    }

    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
