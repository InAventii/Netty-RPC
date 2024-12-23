package com.zzt.client.core;

import java.util.HashSet;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

import com.zzt.client.zk.ZookeeperFactory;

import io.netty.channel.ChannelFuture;

public class ServerWatcher implements CuratorWatcher {
    @Override
    public void process(WatchedEvent event) throws Exception {
    	CuratorFramework client = ZookeeperFactory.create();
    	String path = event.getPath();
    	client.getChildren().usingWatcher(this).forPath(path);
    	List<String> serverPaths = client.getChildren().forPath(path);
    	
    	ChannelManager.realServerPath.clear();
    	for (String serverPath : serverPaths) {
    	    String[] str = serverPath.split("#");
    	    ChannelManager.realServerPath.add(str[0] + "#" + str[1]);
    	}

    	ChannelManager.clear();
    	for (String realServer : ChannelManager.realServerPath) {
    	    String[] str = realServer.split("#");
    	    ChannelFuture channelFuture = TcpClient.b.connect(str[0], Integer.valueOf(str[1]));
    	    ChannelManager.addChannel(channelFuture);
    	}

    }
}

