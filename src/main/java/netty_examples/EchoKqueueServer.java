package netty_examples;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.socket.SocketChannel;

import java.io.IOException;

public class EchoKqueueServer {

    private final int port;
    public EchoKqueueServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        final EchoServerHandler handler = new EchoServerHandler();
        EventLoopGroup group = new KQueueEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(KQueueServerSocketChannel.class)
                    .localAddress(this.port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception{
                            ch.pipeline().addLast(handler);
                        }
                    });
            ChannelFuture future= b.bind().sync();
            future.channel().closeFuture().sync();
        } finally {
            System.out.println("Shutting down the server...");
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        EchoKqueueServer echoKqueueServer = new EchoKqueueServer(Integer.parseInt(args[0]));
        echoKqueueServer.start();
    }
}
