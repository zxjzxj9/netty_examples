package netty_examples;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

public class DatagramChannels {
    void start() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioDatagramChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
                        System.out.println("Received message: " + byteBuf.toString());
                    }
                });
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(1234));
        future.addListener((ChannelFutureListener) channelFuture -> {
            if(channelFuture.isSuccess()) {
                System.out.println("Connect success");
            } else {
                channelFuture.cause().printStackTrace();
            }
        });

        Future<?> future1 = bootstrap.group().shutdownGracefully();
        future1.addListener((FutureListener) future2 -> {
            if(future2.isSuccess()) {
                System.out.println("Connect success");
            } else {
                future2.cause().printStackTrace();
            }
        });
        future.syncUninterruptibly();
    }
}
