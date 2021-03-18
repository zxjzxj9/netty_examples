package netty_examples;

import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Promise;

import java.nio.charset.StandardCharsets;

public class ChannelHandlerEx {
    @Sharable
    public class DiscardHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ReferenceCountUtil.release(msg);
        }
    }

    @Sharable
    public static class SimpleDiscardHandler extends SimpleChannelInboundHandler<Object> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, Object msg) {
            ReferenceCountUtil.release(msg);
        }
    }

    @Sharable
    public static class DiscardChannelOutboundHandler extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
            ReferenceCountUtil.release(msg);

            promise.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if(!channelFuture.isSuccess()) {
                            channelFuture.cause().printStackTrace();
                            channelFuture.channel().close();
                        }
                    }
                }
            );

            promise.setSuccess();
        }
    }

    @Sharable
    public static class SimpleHandler extends ChannelHandlerAdapter {
        private ChannelHandlerContext ctx;

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        public void send(String msg) {
            ctx.writeAndFlush(msg.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Sharable
    public static class SimpleReadHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            System.out.println(msg);
            ctx.fireChannelRead(msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }

    }

    public static void main(String args) {
        ChannelHandler handler1 = new SimpleDiscardHandler();
        ChannelHandler handler2 = new DiscardChannelOutboundHandler();
        ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(handler1);
                socketChannel.pipeline().addLast(handler2);
            }
        };


    }

}
