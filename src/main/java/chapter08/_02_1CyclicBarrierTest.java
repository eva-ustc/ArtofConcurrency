package chapter08;

import common.SleepUtils;

import java.util.concurrent.CyclicBarrier;

public class _02_1CyclicBarrierTest {

    static CyclicBarrier c = new CyclicBarrier(2);

    public static void main(String[] args) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
//                    SleepUtils.second(2);
                    c.await();
                } catch (Exception e) {

                }
                System.out.println(1);
            }
        }).start();

        try {
            c.await();
        } catch (Exception e) {

        }
        System.out.println(2);
    }
}