package com.netty.test.app;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Pranav
 * 10/4/2019
 */
@Slf4j
public class NettyHL7MessageHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        log.info("Connection closed from remote {} to local {} ",ctx.channel().remoteAddress(),ctx.channel().localAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        if(msg instanceof String){
            log.info("{}",msg);
        }else{
            throw new RuntimeException("Expecting msg in string format");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("Connection created from remote {} to local {} ",ctx.channel().remoteAddress(),ctx.channel().localAddress());
    }
}
