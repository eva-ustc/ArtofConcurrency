package chapter08;

import java.util.concurrent.*;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter08
 * @date 2019/3/2 14:15
 * @description God Bless, No Bug!
 */
public class _02_3BankWaterService implements Runnable{

    // 四层屏障
    CyclicBarrier barrier = new CyclicBarrier(4,this);
    // 线程池创建4个线程
    ExecutorService executor = Executors.newFixedThreadPool(4);

    ConcurrentHashMap<String,Integer> map = new ConcurrentHashMap<>();
    public void count(){

        for (int i = 0; i < 4; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    // 统计每个sheet的流水
                    map.put(Thread.currentThread().getName(),1);
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executor.shutdown();
    }

    @Override
    public void run() {
        int result = 0;
        // 统计四个线程的结果
        for (String key : map.keySet()) {
            result +=map.get(key);
        }
        System.out.println(result);

    }

    public static void main(String[] args) {
        _02_3BankWaterService service = new _02_3BankWaterService();
        service.count();
    }
}
