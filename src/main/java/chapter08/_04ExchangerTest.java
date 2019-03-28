package chapter08;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class _04ExchangerTest {

    private static final Exchanger<String> exgr       = new Exchanger<String>();

    private static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {

        threadPool.execute(() -> {
            try {
                String A = "银行流水A";// A录入银行流水
                String B = exgr.exchange(A); // 调用后阻塞park(),直到另一个线程也调用exchange()方法后唤醒unpark()
                System.out.println("交换后的A线程得到数据: "+B);
            } catch (InterruptedException e) {
            }
        });

        threadPool.execute(() -> {
            try {
                String B = "银行流水B";// B录入银行流水
                String A = exgr.exchange(B);
                System.out.println("交换后的B线程得到数据: "+A);
                System.out.println("A和B数据是否一致:" + A.equals(B) + ",A录入的是: " + A
                        + ",B录入的是: " + B);
            } catch (InterruptedException ignored) {
            }
        });
        threadPool.shutdown();

    }
}