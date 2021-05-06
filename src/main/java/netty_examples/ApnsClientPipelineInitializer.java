package netty_examples;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslHandler;
import javax.net.ssl.SSLEngine;


public class ApnsClientPipelineInitializer extends ChannelInitializer<Channel> {
    private final SSLEngine clientEngine;

    public ApnsClientPipelineInitializer(SSLEngine clientEngine) {
        this.clientEngine = clientEngine;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        final ChannelPipeline pipeline = channel.pipeline();
        final SslHandler handler = new SslHandler(clientEngine);
        // handler.setEnableRenegotiation(true);
        pipeline.addLast("ssl", handler);
        // pipeline.addLast("decoder", new ApnsResponseDecoder());
    }
}
