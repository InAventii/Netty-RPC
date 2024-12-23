package com.zzt.client.core;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.curator.framework.CuratorFramework;

import com.alibaba.fastjson.JSONObject;
import com.zzt.client.constant.Constants;
import com.zzt.client.handler.SimpleClientHandler;
import com.zzt.client.param.ClientRequest;
import com.zzt.client.param.Response;
import com.zzt.client.zk.ZookeeperFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import org.apache.curator.framework.api.CuratorWatcher;
public class TcpClient {

    static final Bootstrap b = new Bootstrap();
    static ChannelFuture f = null;
//    static Set<String> realServerPath = new HashSet<String>();
    static {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        b.group(workerGroup); // (2)
        b.channel(NioSocketChannel.class); // (3)
        b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new SimpleClientHandler());
                ch.pipeline().addLast(new StringEncoder());
            }
        });
        CuratorFramework client = ZookeeperFactory.create();
        String host = "localhost";
        int port = 8080;
        try {
        	
        	
        	
        	
            List<String> serverPaths = client.getChildren().forPath(Constants.SERVER_PATH);
            
            CuratorWatcher watcher = new ServerWatcher();

	         client.getChildren().usingWatcher(watcher).forPath(Constants.SERVER_PATH);

         
             
	         for (String serverPath : serverPaths) {
	        	    String[] str = serverPath.split("#");
	        	    ChannelManager.realServerPath.add(str[0] + "#" + str[1]);
	        	    
	        	    ChannelFuture channelFuture = TcpClient.b.connect(str[0], Integer.valueOf(str[1]));
	        	    ChannelManager.addChannel(channelFuture);
	        	}

            if(ChannelManager.realServerPath.size()>0) {
            	String[] hostAndPort = ChannelManager.realServerPath.toArray()[0].toString().split("#");
            	host = hostAndPort[0];
            	port = Integer.valueOf(hostAndPort[1]);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

//        try {
//            f = b.connect(host, port).sync();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } // (5)
    }
    

    public static Response send(ClientRequest request) {
        f = ChannelManager.get();
    	f.channel().writeAndFlush(JSONObject.toJSONString(request));
        f.channel().writeAndFlush("\r\n");
        DefaultFuture df = new DefaultFuture(request);
        return df.get(10000l);
    }

}

