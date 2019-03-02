package pool.thread;

import javafx.concurrent.Worker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name pool.thread
 * @date 2019/2/27 23:00
 * @description God Bless, No Bug!
 */
public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {

    private static final int MAX_WORKER_NUMBERS = 10;
    private static final int DEFAULT_WORKER_NUMBERS = 5;
    private static final int MIN_WORKER_NUMBERS = 1;

    // 任务队列
    private final LinkedList<Job> jobs = new LinkedList<>();

    /**
     * 基本的Java集合类中， 线程安全的有Vector和Hashtable，
     * 其余的ArrayList，LinkedList，HashMap，HashSet，
     *  TreeSet，LinkedHashSet，HashMap，TreeMap都不线程安全。
     * 可以由java.util.Collections来创建线程安全的集合，如：Connections.synchronizedSet(Set<T>);
     */
    // 工作线程列表
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<>());
    // 工作线程的数量
    private int workerNum = DEFAULT_WORKER_NUMBERS;
    // 线程编号
    private AtomicInteger threadNum = new AtomicInteger();

    public DefaultThreadPool() {
        // 初始化时已经把工作线程运行起来了,持续监听jobs任务队列
        initializeWorkers(DEFAULT_WORKER_NUMBERS);
    }
    public DefaultThreadPool(int num) {
        initializeWorkers(num);
    }

    /**
     * 执行线程
     * @param job
     */
    @Override
    public void execute(Job job) {
        if (job != null) {
            synchronized (jobs) { // 添加到任务队列,唤醒工作线程
                jobs.addLast(job);
                jobs.notify();
            }
        }
    }
    class Worker implements Runnable {
        boolean running = true; //标识线程是否在工作
        @Override
        public void run() {
            while (running) {
                Job job;
                synchronized (jobs) {
                    while (jobs.isEmpty()){
                        try {
                            jobs.wait();
                        } catch (InterruptedException e) {
                            // 感知到外部对WorkerThread的中断操作
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    job = jobs.removeFirst();
                }
                if (job != null) {
                    job.run();
                }
            }
        }

        public void shutdown() {
            running = false;
        }
    }



    @Override
    public void addWorker(int num) {
        synchronized (jobs) {
            // 判断是否超出最大工作线程数
            if (num + this.workerNum > MAX_WORKER_NUMBERS) {
                num = MAX_WORKER_NUMBERS;
            }
            initializeWorkers(num);
        }
    }

    @Override
    public void removeWorker(int num) {
        synchronized (jobs) {
            if (num > workerNum) {
                throw new IllegalArgumentException("beyond MaxWorkerNum");
            }
            int count = 0;
            while (count < num) {
                Worker worker = workers.get(workerNum);
                if (workers.remove(worker)) {
                    worker.shutdown();
                    count++;
                }
            }
            this.workerNum -= num;
        }
    }

    @Override
    public void shutdown() {
        for (Worker worker : workers) {
            worker.shutdown();
        }
    }

    @Override
    public int getJobSize() {
        return jobs.size();
    }

    private void initializeWorkers(int numbers) {
        for (int i = 0; i < numbers; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = new Thread(worker, "ThreadPool-Worker-" + threadNum.incrementAndGet());
            thread.start();
        }
    }
}
