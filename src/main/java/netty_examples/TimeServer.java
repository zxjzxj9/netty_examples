package netty_examples;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeServer {
    private final int port;

    public TimeServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        final TimeServerHandler timeServerHandler = new TimeServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel (SocketChannel ch) throws Exception{
                            ch.pipeline().addLast(timeServerHandler);
                        }
                    });
            ChannelFuture future = b.bind().sync();
            future.channel().closeFuture().sync();
        } finally {
            System.out.println("Shutting down the server...");
            group.shutdownGracefully().sync();
        }

    }

    public static void main(String[] args) throws Exception{
        if(args.length != 1) {
            System.err.println(
                    "Usage: " + TimeServer.class.getSimpleName() + " <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        new TimeServer(port).start();
    }
}
