package chapter3;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter3
 * @date 2019/2/24 22:16
 * @description God Bless, No Bug!
 */
public class Sub3_2_4ReorderExample {

    int     a    = 0;
    boolean flag = false;

    public void writer() {
        a = 1; //1
        flag = true; //2
    }

    public void reader() {
        if (flag) { //3
            int i = a * a; //4
            System.out.println(i);
            //s¡­¡­
        }
    }

    public static void main(String[] args) {
        Sub3_2_4ReorderExample test = new Sub3_2_4ReorderExample();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                test.writer();
            }
        });
        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                test.reader();
            }
        });
        a.start();
        b.start();
    }
}
