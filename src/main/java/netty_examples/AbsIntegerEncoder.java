package netty_examples;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        while(byteBuf.readableBytes() >= 4) {
            int num = Math.abs(byteBuf.readInt());
            list.add(num);
        }
    }
}
