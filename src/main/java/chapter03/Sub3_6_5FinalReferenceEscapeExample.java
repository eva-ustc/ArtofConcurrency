package chapter03;

/**
 * 在构造函数返回前,被构造对象的引用不能为其他线程所见,
 * 因为此时final域可能还没初始化
 */
public class Sub3_6_5FinalReferenceEscapeExample {
    public static void main(String[] args) {
        Sub3_6_5FinalReferenceEscapeExample test = new Sub3_6_5FinalReferenceEscapeExample();
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
            }
        }).start();
    }

    final int i;
    static Sub3_6_5FinalReferenceEscapeExample obj;

    public Sub3_6_5FinalReferenceEscapeExample() {
        obj = this; //2 this对象在此"逸出"
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        i = 1; //写final域
    }

    public static void writer() {
        new Sub3_6_5FinalReferenceEscapeExample();
    }

    public static void reader() {
        if (obj != null) { //3
//            int temp = obj.i; //4
            System.out.println(obj.i);
        }
    }
}