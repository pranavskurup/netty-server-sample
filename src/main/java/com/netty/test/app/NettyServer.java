package com.netty.test.app;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author Pranav
 * 10/4/2019
 */
@RequiredArgsConstructor
public class NettyServer {
    private final int port;
    private final List<Supplier<ChannelHandler>> supplier;
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    @Getter
    private Channel channel;
    @Getter
    private ChannelFuture channelFuture;

    public void connectLoop() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            supplier.forEach(handlerSupplier -> pipeline.addLast(handlerSupplier.get()));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(port).sync();


            this.channel = f.channel();
            this.channelFuture = f;
            //f.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println("Exception in server thread");
        }
    }

    public void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
