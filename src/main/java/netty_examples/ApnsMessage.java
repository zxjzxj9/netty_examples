package netty_examples;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteOrder;

public final class ApnsMessage {
    private static final byte COMMAND = (byte) 1;

    public ByteBuf toBuffer() {
    //     short size = (short) (1 + // Command
    //             4 + // Identifier
    //             4 + // Expiry
    //             2 + // DT length header
    //             32 + //DS length
    //             2 + // body length header
    //             body.length);
        short size = 32;
        ByteBuf buf = Unpooled.buffer(size).order(ByteOrder.BIG_ENDIAN);
    //     buf.writeByte(COMMAND);
    //     buf.writeInt(identifier);
    //     buf.writeInt(expiryTime);
    //     buf.writeShort((short) deviceToken.length);
    //     buf.writeBytes(deviceToken);
    //     buf.writeShort((short) body.length);
    //     buf.writeBytes(body);
        return buf;
    }
}