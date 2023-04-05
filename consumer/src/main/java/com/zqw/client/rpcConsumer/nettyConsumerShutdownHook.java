package com.zqw.client.rpcConsumer;

import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class nettyConsumerShutdownHook implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    public Channel channel;

    @Autowired
    public NioEventLoopGroup group;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        channel.close();
        group.shutdownGracefully();
        log.info("关闭成功");
    }

}
