package com.zzt.netty.client;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

import java.util.concurrent.TimeUnit;

import com.zzt.netty.handler.SimpleClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;

public class NettyClient {

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 8080;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                	ch.pipeline().addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()[0]));
                	ch.pipeline().addLast(new StringDecoder());
                	ch.pipeline().addLast(new IdleStateHandler(60, 45, 20, TimeUnit.SECONDS));
                	ch.pipeline().addLast(new SimpleClientHandler());
                	ch.pipeline().addLast(new StringEncoder());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)
            f.channel().writeAndFlush("helloServer");
            f.channel().writeAndFlush("\r\n");
            

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
            Object result = f.channel().attr(AttributeKey.valueOf("sssss")).get();
            System.out.println("Get response from server:"+result.toString());
        } finally {
            workerGroup.shutdownGracefully();
        }
    }



}
