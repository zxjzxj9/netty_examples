package netty_examples;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChannelHandlerEx {
    @ChannelHandler.Sharable
    public class DiscardHandler extends ChannelInboundHandlerAdapter {

    }
}
