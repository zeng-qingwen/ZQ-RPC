package com.zqw.Server.nettyserver;

import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class nettyProviderShutdownHook implements ApplicationListener<ContextClosedEvent> {
    @Autowired
    CuratorFramework curatorFramework;
    @Autowired
    NioEventLoopGroup boss;
    @Autowired
    NioEventLoopGroup worker;
    @Autowired
    Channel channel;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        this.channel.close();
        this.boss.shutdownGracefully();
        this.worker.shutdownGracefully();
        CloseableUtils.closeQuietly(curatorFramework);
        System.out.println("关闭");
    }
}
