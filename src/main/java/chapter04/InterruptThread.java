package chapter04;

import common.SleepUtils;

import java.util.concurrent.TimeUnit;

public class InterruptThread {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                while (true) {
                    // 判断当前线程是否被中断
                    if (this.isInterrupted()) {
                        System.out.println("线程中断");
                        Thread.interrupted(); // 中断标志位复位方法
                        SleepUtils.second(2);
//                        break;
                    }else {

                        System.out.println("线程未中断...");
                    }
                }

//                System.out.println("已跳出循环,线程中断!");
            }
        };
        t1.start();
        TimeUnit.SECONDS.sleep(2);
        t1.interrupt();
    }
}