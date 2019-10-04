package com.netty.test.app;


import java.util.Arrays;

/**
 * @author Pranav
 * 10/4/2019
 */
public class SocketServerRunner {
    public static void main(String[] args) {
        try {
            ServerFlow.OpenServerPort(9000, Arrays.asList(NettyHL7DecoderHandler::new, NettyHL7MessageHandler::new)).channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
