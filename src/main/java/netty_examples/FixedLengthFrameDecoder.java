package netty_examples;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
    private final int frameLength;
    private static final int MAX_FRAME_SIZE = 1024;

    public FixedLengthFrameDecoder(int frameLength) {
        if(frameLength < 0) throw new IllegalArgumentException(
                "frameLength must be a positive integer: " + frameLength);
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        int readable = byteBuf.readableBytes();
        if (readable > MAX_FRAME_SIZE) {
            byteBuf.skipBytes(readable);
            throw new TooLongFrameException("Frame too big!");
        }
        while(byteBuf.readableBytes() >= frameLength) {
            ByteBuf buf = byteBuf.readBytes(frameLength);
            out.add(buf);
        }
    }
}
