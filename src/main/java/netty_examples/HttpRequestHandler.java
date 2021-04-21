package netty_examples;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    // private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class
            .getProtectionDomain()
            .getCodeSource().getLocation();

        try {
            String path = location.toURI() + "/index.html";
            path = !path.contains("file:") ? path:path.substring(5);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to locate index.html", e);
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if(wsUri.equalsIgnoreCase(req.uri())) {
            ctx.fireChannelRead(req.retain());
        } else {

        }
    }
}
