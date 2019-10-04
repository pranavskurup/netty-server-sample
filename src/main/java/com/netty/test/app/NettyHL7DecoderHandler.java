package com.netty.test.app;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author Pranav
 * 10/4/2019
 */
@Slf4j
public class NettyHL7DecoderHandler extends DelimiterBasedFrameDecoder {
    private static final int MAX_FRAME_LENGTH = Integer.MAX_VALUE;
    private static final char startByte = 0x0b; // 11 decimal
    private static final char endByte1 = 0x1c; // 28 decimal
    private static final char endByte2 = 0x0d; // 13 decimal
    private Charset charset = Charset.defaultCharset();

    public NettyHL7DecoderHandler() {
        super(MAX_FRAME_LENGTH, true, Unpooled.copiedBuffer(
                new char[]{endByte1, endByte2},
                Charset.defaultCharset()));
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        ByteBuf buf = (ByteBuf) super.decode(ctx, buffer);
        if (buf != null) {
            try {
                int pos = buf.bytesBefore((byte) startByte);
                if (pos >= 0) {
                    ByteBuf msg = buf.readerIndex(pos + 1).slice();
                    log.debug("Message ends with length {}", msg.readableBytes());
                    return asString(msg);
                } else {
                    throw new DecoderException("Did not find start byte " + (int) startByte);
                }
            } finally {
                // We need to release the buf here to avoid the memory leak
                buf.release();
            }
        }
        // Message not complete yet - return null to be called again
        log.debug("No complete messages yet at position {}", buffer.readableBytes());
        return null;
    }

    private String asString(ByteBuf msg) {
//        String s = msg.toString(charset);
//        if (config.isConvertLFtoCR()) {
//            return s.replace('\n', '\r');
//        }
        return msg.toString(charset);
    }
}
