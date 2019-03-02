package chapter05;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter05
 * @date 2019/2/28 20:14
 * @description God Bless, No Bug!
 *
 *  自定义互斥锁
 */
public class Sub5_2Mutex implements Lock {

    // 静态内部类 队列同步器
    private static class Sync extends AbstractQueuedSynchronizer{
        @Override
        protected boolean isHeldExclusively() {
            return getState()==1;
        }

        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0,1)){
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState()==0){
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }
        Condition newCondition(){
            return new ConditionObject();
        }
    }

    private final Sync sync = new Sync();
    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
