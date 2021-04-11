package netty_examples;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

public class HttpsCodecInitializer extends ChannelInitializer<Channel> {
    private final SslContext ctx;
    private final boolean isClient;

    public HttpsCodecInitializer(SslContext ctx, boolean isClient) {
        this.ctx = ctx;
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        SSLEngine engine = ctx.newEngine(channel.alloc());

        pipeline.addLast("ssl", new SslHandler(engine));
        if(isClient) {
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            pipeline.addLast("codec", new HttpServerCodec());
        }
    }
}
