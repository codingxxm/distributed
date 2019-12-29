package bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author CodingXXM
 * @desc
 * @date 2019/12/29 0:18
 **/
public class SocketServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        while (true) {
            System.out.println("等待连接...");
            Socket accept = serverSocket.accept();
            System.out.println("有连接加入");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        handle(accept);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void handle(Socket socket) throws IOException {
        System.out.println("当前是" + Thread.currentThread().getId() + "线程在处理");
        byte[] buf = new byte[1024];
        System.out.println("准备read");
        int read = socket.getInputStream().read(buf);
        System.out.println("read完成");
        System.out.println("data:" + new String(buf));
        socket.getOutputStream().write("hello client".getBytes());
        socket.getOutputStream().flush();
    }
}
