package bio;

import java.io.IOException;
import java.net.Socket;

/**
 * @author CodingXXM
 * @desc
 * @date 2019/12/29 0:34
 **/
public class SocketClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9999);
        socket.getOutputStream().write("hello server".getBytes());
        socket.getOutputStream().flush();
        System.out.println("向服务端发送数据结束");
        byte[] bytes = new byte[1024];
        socket.getInputStream().read(bytes);
        System.out.println("data from server:" + new String(bytes));
        socket.close();
    }
}
