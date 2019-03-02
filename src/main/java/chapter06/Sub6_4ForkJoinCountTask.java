package chapter06;

import sun.misc.Unsafe;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter06
 * @date 2019/3/1 20:10
 * @description God Bless, No Bug!
 */
public class Sub6_4ForkJoinCountTask extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 2; // 阈值
    private int start;
    private int end;

    public Sub6_4ForkJoinCountTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;

        // 如果任务足够小就计算任务
        boolean canCompute = (end - start) <= THRESHOLD;
        if (canCompute) { // 计算逻辑
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            // 任务大于阈值,就分裂成两个子任务计算
            int middle = (start + end) / 2;
            Sub6_4ForkJoinCountTask leftTask = new Sub6_4ForkJoinCountTask(start, middle);
            Sub6_4ForkJoinCountTask rightTask = new Sub6_4ForkJoinCountTask(middle + 1, end);

            // 执行子任务
            leftTask.fork();
            rightTask.fork();

            // 等待子任务执行完,并得到其结果
            int leftResult = leftTask.join();
            int rightResult = rightTask.join();

            // 合并子任务
            sum = leftResult + rightResult;
        }
        return sum;
    }

    public static void main(String[] args) {

        // 任务需要通过ForkJoinPool来执行
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Sub6_4ForkJoinCountTask task = new Sub6_4ForkJoinCountTask(1, 10);
        // 执行一个任务
        Future<Integer> result = forkJoinPool.submit(task);
        try {
            System.out.println(result.get());
        } catch (Exception e) {
        }
        Unsafe unsafe;
    }
}
