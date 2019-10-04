package com.netty.test.app;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author Pranav
 * 10/4/2019
 */

public class ServerFlow {
    public static ChannelFuture OpenServerPort(int port, List<Supplier<ChannelHandler>> supplier) {
        try {
            NettyServer nettyServer = new NettyServer(port, supplier);
            nettyServer.connectLoop();
            return nettyServer.getChannelFuture();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
