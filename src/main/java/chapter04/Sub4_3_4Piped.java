package chapter04;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name chapter04
 * @date 2019/2/26 22:26
 * @description God Bless, No Bug!
 */
public class Sub4_3_4Piped {
    public static void main(String[] args) throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream();
        out.connect(in);

        new Thread(new Print(in)).start();
        int temp = 0;
        try {
            while ((temp=System.in.read())!=-1){
                out.write(temp);
            }
        }finally {
            out.close();
        }
    }

    static class Print implements Runnable{

        PipedInputStream in;
        public Print(PipedInputStream in){
            this.in = in;
        }
        @Override
        public void run() {
            int receive = 0;
            try {

                while ((receive=in.read())!=-1){
                    System.out.print((char) receive);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
