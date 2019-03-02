package pool.connection;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name pool
 * @date 2019/2/27 21:28
 * @description God Bless, No Bug!
 *
 *  连接池
 */
public class ConnectionPool {
    private LinkedList<Connection> pool = new LinkedList<>();
    /**
     * 初始化连接池
     */
    public ConnectionPool(int initialSize){
        if (initialSize<1) {
            throw new RuntimeException("初始化参数异常!");
        }
        for (int i = 0; i < initialSize; i++) {
            Connection connection = ConnectionDriver.createConnection();
            pool.addLast(connection);
        }
    }

    /**
     * 获取连接,在mills内无法获取到连接，将会返回null
     * @return
     */
    public Connection getConnection(long mills) throws InterruptedException {

        synchronized (pool){
            if (mills <=0){ // 无超时限制
                while (pool.isEmpty()) {
                    pool.wait();
                }
                return pool.removeFirst();
            }else { // 有超时限制
                long future = System.currentTimeMillis()+mills; // 超时点
                long remaining = mills; // 剩余时间
                while (pool.isEmpty() && remaining>0){
                    pool.wait(remaining);
                    remaining = future - System.currentTimeMillis();
                }
                Connection result = null;
                if (!pool.isEmpty()){
                    result = pool.removeFirst();
                }
                return result;
            }
        }
    }

    /**
     * 释放连接
     */
    public void releaseConnection(Connection connection){
        if (connection!=null){

            synchronized (pool){
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }
}
