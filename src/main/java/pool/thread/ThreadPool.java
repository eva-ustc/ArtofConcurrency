package pool.thread;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name pool.thread
 * @date 2019/2/27 22:50
 * @description God Bless, No Bug!
 */
public interface ThreadPool<Job extends Runnable> {
    void execute(Job job);
    void addWorker(int num);
    void shutdown();
    void removeWorker(int num);
    int getJobSize();
}
