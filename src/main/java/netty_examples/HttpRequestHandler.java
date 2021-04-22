package netty_examples;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

import static io.netty.handler.codec.http.HttpHeaderNames.*;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class
            .getProtectionDomain()
            .getCodeSource().getLocation();

        try {
            String path = location.toURI() + "/index.html";
            path = !path.contains("file:") ? path:path.substring(5);
            INDEX = new File(path);
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
            if(HttpUtil.is100ContinueExpected(req)) {
                // send 100 continue response
                FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
                ctx.writeAndFlush(resp);
            }
        }

        RandomAccessFile file = new RandomAccessFile(INDEX, "r");
        HttpResponse resp = new DefaultHttpResponse(req.protocolVersion(), HttpResponseStatus.OK);
        resp.headers().set(
                CONTENT_TYPE,
                "text/plain; charset=UTF-8");
        boolean keepAlive = HttpUtil.isKeepAlive(req);
        if(keepAlive) {
            resp.headers().set(
                CONTENT_LENGTH,
                file.length());
            resp.headers().set(
                CONNECTION,
                "keep-alive"
            );
        }

        ctx.write(resp);

        if(ctx.pipeline().get(SslHandler.class) == null) {
            ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
        } else {
            ctx.write(new ChunkedNioFile(file.getChannel()));
        }

        ChannelFuture future = ctx.writeAndFlush(
                LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
