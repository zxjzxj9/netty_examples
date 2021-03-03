package netty_examples;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class PlainNioServer {

    public void serve(int port) throws IOException {
        ServerSocketChannel ch = ServerSocketChannel.open();
        ch.configureBlocking(false);
        ServerSocket socket = ch.socket();
        InetSocketAddress addr = new InetSocketAddress(port);
        socket.bind(addr);

        Selector selector = Selector.open();
        ch.register(selector, SelectionKey.OP_ACCEPT);
        final ByteBuffer msg = ByteBuffer.wrap("Hi! \r\n".getBytes(StandardCharsets.UTF_8));

        for(;;) {
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            // Iterator<SelectionKey> iterator = readyKeys.iterator();
            for(SelectionKey k: readyKeys) {
                try {
                    if(k.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) k.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());
                        System.out.println("Accept connection from: " + client);
                        if(k.isWritable()) {
                            SocketChannel client_tmp = (SocketChannel) k.channel();
                            ByteBuffer buffer =  (ByteBuffer) k.attachment();
                            while(buffer.hasRemaining()) {
                                if (client_tmp.write(buffer) == 0) break;
                            }
                            client_tmp.close();
                        }
                    }
                } catch (IOException e) {
                    k.cancel();
                    try {
                        k.channel().close();
                    } catch (IOException cex) {
                        // ignore on close
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        PlainOioServer server = new PlainOioServer();
        server.serve(Integer.parseInt(args[0]));
    }
}
