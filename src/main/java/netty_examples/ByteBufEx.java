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
    }

    public static void main(String[] args) {
        compositeBuffer("Hello, ", "World!");
    }
}
