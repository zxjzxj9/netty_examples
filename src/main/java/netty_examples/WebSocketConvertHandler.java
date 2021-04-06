package netty_examples;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

public class WebSocketConvertHandler extends MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandler.MyWebSocketFrame> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MyWebSocketFrame myWebSocketFrame, List<Object> list) throws Exception {
        ByteBuf payload = myWebSocketFrame.getData().duplicate().retain();
        switch (myWebSocketFrame.getType()) {
            case BINARY:
                list.add(new BinaryWebSocketFrame(payload));
                break;
            case PING:
                list.add(new PingWebSocketFrame(payload));
            case PONG:
                list.add(new PongWebSocketFrame(payload));
                break;
            case TEXT:
                list.add(new TextWebSocketFrame(payload));
                break;
            case CONTINUATION:
                list.add(new ContinuationWebSocketFrame(payload));
                break;
            case CLOSE:
                list.add(new CloseWebSocketFrame(true, 0, payload));
                break;
            default:
                throw new IllegalStateException("Unsupported websocket msg " + myWebSocketFrame);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List<Object> list) throws Exception {

    }

    public static final class MyWebSocketFrame {
        public enum FrameType {
            BINARY,
            CLOSE,
            PING,
            PONG,
            TEXT,
            CONTINUATION
        }

        private final FrameType type;
        private final ByteBuf data;

        MyWebSocketFrame(FrameType type, ByteBuf data) {
            this.type = type;
            this.data = data;
        }

        public FrameType getType() {
            return type;
        }

        public ByteBuf getData() {
            return data;
        }


    }
}
