package netty_examples;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

// Add examples of ByteBuf in Netty
public class ByteBufEx {

    public static void compositeBuffer(String s1, String s2) {
        CompositeByteBuf buf = Unpooled.compositeBuffer();
        ByteBuf header = Unpooled.copiedBuffer(s1.getBytes(StandardCharsets.UTF_8));
        ByteBuf body = Unpooled.copiedBuffer(s2.getBytes(StandardCharsets.UTF_8));
        buf.addComponents(header, body);
        // System.out.println(buf.toString());
        for(ByteBuf b: buf) {
            System.out.println(b.toString());
        }

        while(buf.isReadable()) {
            System.out.println((char)buf.readByte());
        }

        for(ByteBuf b: buf) {
            if (b.hasArray()) {
                byte[] array = b.array();
                int offset = b.arrayOffset();
                int readIdx = b.readerIndex();
                int sz = b.readableBytes();
                System.out.println(new String(array, offset + readIdx, sz));
            }
        }
    }


    public static void sliceBuffer(String s1) {
        ByteBuf buf = Unpooled.copiedBuffer(s1.getBytes(StandardCharsets.UTF_8));

        ByteBuf buf1 = buf.slice(0, 5);
        byte[] array = buf1.array();
        int offset = buf1.arrayOffset();
        int readIdx = buf1.readerIndex();
        int sz = buf1.readableBytes();
        System.out.println(buf.toString(StandardCharsets.UTF_8));
        System.out.println(new String(array, offset+readIdx, sz));
    }

    public static void writeBuffer(String s1) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(s1.getBytes(StandardCharsets.UTF_8));
        while(buf.isReadable()) {
            System.out.println((char) buf.readByte());
        }
    }

    public static void main(String[] args) {
        compositeBuffer("Hello, ", "World!");
        writeBuffer("Hello");
        sliceBuffer("Hello World!");
    }
}
