package chapter10;

import java.util.concurrent.*;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter10
 * @date 2019/3/2 15:57
 * @description God Bless, No Bug!
 */
public class ExecutorTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> result = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 1+2;
            }
        });
        System.out.println(result.get());
        executor.shutdown();
    }
}
