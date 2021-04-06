package netty_examples;

import io.netty.channel.CombinedChannelDuplexHandler;

public class CombinedByteIntegerCodec extends CombinedChannelDuplexHandler<ByteToIntegerDecoder, IntegerToByteEncoder> {
    public CombinedByteIntegerCodec() {
        super(new ByteToIntegerDecoder(), new IntegerToByteEncoder());
    }
}
