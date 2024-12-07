package com.zzt.client.core;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.curator.framework.CuratorFramework;

import com.zzt.client.zk.ZookeeperFactory;

import io.netty.channel.ChannelFuture;

public class ChannelManager {

    static Set<String> realServerPath = new HashSet<>();
    public static CopyOnWriteArrayList<ChannelFuture> channelFutures = new CopyOnWriteArrayList<>();
    private static CuratorFramework client = ZookeeperFactory.create();
    private static final String POSITION_PATH = "/distributed/position";
    private static final String LOCK_PATH = "/lock/position";

    static {
        try {
            // 初始化 Zookeeper 中的计数器路径
            if (client.checkExists().forPath(POSITION_PATH) == null) {
                client.create().creatingParentsIfNeeded().forPath(POSITION_PATH, "0".getBytes());
            }

            // 初始化 Zookeeper 中的锁路径（父路径）
            if (client.checkExists().forPath("/lock") == null) {
                client.create().forPath("/lock");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeChannel(ChannelFuture channel) {
        channelFutures.remove(channel);
    }

    public static void addChannel(ChannelFuture channel) {
        channelFutures.add(channel);
    }

    public static void clear() {
        channelFutures.clear();
        try {
            // 清空时重置分布式计数器
            client.setData().forPath(POSITION_PATH, "0".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ChannelFuture get() {
        ChannelFuture channelFuture = null;
        String lockNode = null;

        try {
            // 创建临时节点作为锁，同时创建父路径（如必要）
            lockNode = client.create()
                .creatingParentsIfNeeded()
                .withMode(org.apache.zookeeper.CreateMode.EPHEMERAL)
                .forPath(LOCK_PATH);

            String positionData = new String(client.getData().forPath(POSITION_PATH));
            int position = Integer.parseInt(positionData);
            int size = channelFutures.size();

            if (position >= size) {
                channelFuture = channelFutures.get(0); // 重置为第一个通道
                client.setData().forPath(POSITION_PATH, "1".getBytes()); // 重置计数器为 1
            } else {
                channelFuture = channelFutures.get(position);
                client.setData().forPath(POSITION_PATH, String.valueOf(position + 1).getBytes()); // 更新计数器
            }
        } catch (org.apache.zookeeper.KeeperException.NodeExistsException e) {
            // 如果锁节点已经存在，说明其他进程持有锁
            System.out.println("Lock already held, retrying...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (lockNode != null) {
                try {
                    // 释放锁，删除临时节点
                    client.delete().forPath(LOCK_PATH);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (!channelFuture.channel().isActive()) {
            channelFutures.remove(channelFuture); // 从列表中移除不活跃的通道
            return get(); // 尝试获取下一个通道
        }

        return channelFuture;
    }
}
