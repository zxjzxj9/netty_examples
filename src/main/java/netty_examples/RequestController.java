package netty_examples;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

public class RequestController extends IdleStateAwareChannelUpstreamHandler {
    @Override
    public void channelIdle(ChannelHandlerContext ctx,
                            IdleStateEvent e) throws Exception {
        // Do something
    }

    @Override public void channelConnected(ChannelHandlerContext ctx,
                                           ChannelStateEvent e) throws Exception {
        // Do something
    }

    @Override public void messageReceived(ChannelHandlerContext ctx,
                                          MessageEvent e) throws Exception {
        // Do something
    }
}
