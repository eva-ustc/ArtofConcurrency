package pool.connection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name pool
 * @date 2019/2/27 21:29
 * @description God Bless, No Bug!
 *  连接代理类
 */
public class ConnectionDriver {

    private static class ConnectionProxy implements InvocationHandler{

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("commit")){

                TimeUnit.MILLISECONDS.sleep(100); // 提交时睡眠100毫秒
            }
            return null;
        }
    }

    public static final Connection createConnection(){

        return (Connection) Proxy.newProxyInstance(ConnectionDriver.class.getClassLoader(),
                new Class[]{Connection.class},new ConnectionProxy());
    }

}
