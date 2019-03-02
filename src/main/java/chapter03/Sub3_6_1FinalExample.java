package chapter03;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter03
 * @date 2019/2/25 21:56
 * @description God Bless, No Bug!
 *  final重排规则:
 *      1 JMM禁止把final域的写操作重拍到构造函数之外;
 *      2 读取包含final成员域的引用时,final域已经被初始化
 *
 *      3 final域为引用时,在"构造函数"里面给final引用的成员域赋值 与 随后在构造函数外将被构造对象的引用赋值给其他引用 不能重排
 *          即 包含final引用域,且在构造函数里给final引用的成员域赋值 这个操作完成后才能将该对象赋给其他引用
 */
public class Sub3_6_1FinalExample {
    public static void main(String[] args) {
        Sub3_6_1FinalExample test = new Sub3_6_1FinalExample();
        new Thread(new Runnable() {
            @Override
            public void run() {
                test.writer();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                test.reader();
                System.out.println(test.i);
            }
        }).start();
    }
    int                 i;  //普通变量
    final int           j;  //final变量
    static Sub3_6_1FinalExample obj;

    public Sub3_6_1FinalExample() { //构造函数
        i = 1; //写普通域
        j = 2; //写final域
    }

    public static void writer() { //写线程A执行
        obj = new Sub3_6_1FinalExample();
    }

    public static void reader() { //读线程B执行
        Sub3_6_1FinalExample object = obj; //读对象引用
        System.out.println(obj==null);
        int a = object.i; //读普通域
        int b = object.j; //读final域
    }
}
