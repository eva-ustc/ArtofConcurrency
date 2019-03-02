package pool.thread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author LRK
 * @project_name artofconcurrency
 * @package_name pool.thread
 * @date 2019/2/28 18:11
 * @description God Bless, No Bug!
 */
public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {
        SimpleHttpServer.start();
    }

    // 线程池
    static ThreadPool<HttpRequestHandler> pool = new DefaultThreadPool<>(3);
    // 根路径,服务Socket,端口号
    static String basePath = SimpleHttpServer.class.getResource("").getPath();
    static int port = 8080;

    public static void setPort(int port) {
        if (port > 0) {
            SimpleHttpServer.port = port;
        }
    }

    public static void setBasePath(String basePath) {
        if (basePath != null && new File(basePath).exists() && new File(basePath).isDirectory()) {
            SimpleHttpServer.basePath = basePath;
        }
    }

    // 启动线程处理请求
    public static void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket;
        while (true) {
            if ((socket = serverSocket.accept()) != null) {
                pool.execute(new HttpRequestHandler(socket));
            }
        }
//        serverSocket.close();
    }

    // 请求处理线程
    static class HttpRequestHandler implements Runnable {
        private Socket socket = null;

        public HttpRequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader reader = null;
            BufferedReader br = null;
            PrintWriter out = null;
            InputStream in = null;
            String line;
            // 1 构建文件路径
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String header = reader.readLine();
                System.out.println(header);
                String filePath = basePath + header.split(" ")[1];
                System.out.println(filePath);
                out = new PrintWriter(socket.getOutputStream());

                // 2 根据文件类型构建返回数据
                if (filePath.endsWith("jpg") || filePath.endsWith("ico")) {
                    // 图像文件使用字节流传输
                    in = new FileInputStream(filePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i = 0;
                    while ((i = in.read()) != -1) {
                        baos.write(i);
                    }
                    byte[] array = baos.toByteArray();
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: image/jpeg");
                    out.println("Content-Length: " + array.length);
                    out.println("");
                    socket.getOutputStream().write(array, 0, array.length);
                } else {
                    // 文字直接使用Socket传输
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: text/html; charset=UTF-8");
                    out.println("");
                    while ((line = br.readLine()) != null) {
                        out.println(line);
                    }
                }
                out.flush();
                // 3 返回数据并关闭数据流
            } catch (IOException e) {
                out.println("HTTP/1.1 500");
                out.println("");
                out.flush();
            } finally {
                close(br, in, reader, out, socket);
            }

        }

        private void close(Closeable... closeables) {
            if (closeables != null) {
                for (Closeable closeable : closeables) {
                    try {
                        if (closeable!=null)
                        closeable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
