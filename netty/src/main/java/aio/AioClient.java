package aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

/**
 * @author CodingXXM
 * @desc
 * @date 2019/12/30 22:08
 **/
public class AioClient {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999)).get();
        socketChannel.write(ByteBuffer.wrap("HelloServer".getBytes()));
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        Integer len = socketChannel.read(byteBuffer).get();
        if (len != -1) {
            System.out.println("data from server:" + new String(byteBuffer.array(), 0 ,len) );
        }
    }
}
