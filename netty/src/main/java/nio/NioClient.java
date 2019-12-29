package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author CodingXXM
 * @desc
 * @date 2019/12/29 23:58
 **/
public class NioClient {

    private Selector selector;

    public static void main(String[] args) throws IOException {
        NioClient nioClient = new NioClient();
        nioClient.init("127.0.0.1", 9999);
        nioClient.connect();
    }

    public void init(String ip, int port) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        this.selector = Selector.open();
        channel.connect(new InetSocketAddress(ip, port));
        channel.register(selector, SelectionKey.OP_CONNECT);
    }

    public void connect() throws IOException {
        while (true) {
            int select = selector.select();
            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                    }
                    channel.configureBlocking(false);
                    ByteBuffer wrap = ByteBuffer.wrap("HelloServer".getBytes());
                    channel.write(wrap);
                    channel.register(this.selector, SelectionKey.OP_READ);
                }else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer allocate = ByteBuffer.allocate(512);
                    int len = channel.read(allocate);
                    if (len != -1) {
                        System.out.println("data from server:" + new String(allocate.array(), 0, len));
                    }
                }
            }
        }
    }
}
