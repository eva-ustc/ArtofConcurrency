package chapter08;

import java.util.concurrent.CountDownLatch;

public class _01CountDownLatchTest {

    static CountDownLatch c = new CountDownLatch(3);

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+" remains: "+c.getCount());
            c.countDown();

        },"inner Thread").start();
        new Thread(new MyRunnable(c),"MyRunnable").start();
        System.out.println(Thread.currentThread().getName()+" remains: "+c.getCount());
        c.countDown();

        c.await();
        System.out.println(Thread.currentThread().getName()+" remains: "+c.getCount());
    }
}
class MyRunnable implements Runnable{

    private CountDownLatch counter;

    public MyRunnable(CountDownLatch counter){
        this.counter = counter;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName()+ " remains: "+counter.getCount());
            counter.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}