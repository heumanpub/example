package com.heuman.example.netty.echo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author heuman
 * @date 2019/5/26 19:45
 */
public class SimpleEchoClient {

    private static final String host = "127.0.0.1";
    private static final Integer port = 6688;

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    final NioEventLoopGroup group = new NioEventLoopGroup();
                    try {
                        Bootstrap bootstrap = new Bootstrap();
                        bootstrap.group(group)
                                .channel(NioSocketChannel.class)
                                .remoteAddress(host, port)
                                .handler(new ChannelInitializer<SocketChannel>() {
                                    protected void initChannel(SocketChannel ch) throws Exception {
                                        ch.pipeline().addLast(new EchoClientHandler());
                                    }
                                });
                        ChannelFuture channelFuture = bootstrap.connect().sync();
                        channelFuture.channel().closeFuture().sync();
                    } catch (Exception e) {
                        System.out.println("communication exception");
                        e.printStackTrace();
                    }finally {
                        try {
                            group.shutdownGracefully().sync();
                        } catch (InterruptedException e) {
                            System.out.println("channel group close exception");
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

}
