package netty_examples;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.util.List;

public class LogEventDecoder extends MessageToMessageEncoder<DatagramPacket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, DatagramPacket packet, List<Object> list) throws Exception {
        ByteBuf buf = packet.content();

        int idx = buf.indexOf(0, buf.readableBytes(), LogEvent.SEPARATOR);
        String filename = buf.slice(0, idx) .toString(CharsetUtil.UTF_8);
        String logMsg = buf.slice(idx + 1, buf.readableBytes()).toString(CharsetUtil.UTF_8);
        LogEvent event = new LogEvent(packet.sender(), logMsg, filename, System.currentTimeMillis() );

        list.add(event);
    }
}
