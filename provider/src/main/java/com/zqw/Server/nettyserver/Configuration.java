package com.zqw.Server.nettyserver;

import com.zqw.Server.utils.CuratorUtils;
import com.zqw.common.serial.HessianSerializer;
import com.zqw.common.serial.Serializer;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    public NioEventLoopGroup boss(){
        return new NioEventLoopGroup(1);
    }

    @Bean
    public NioEventLoopGroup worker(){
        return new NioEventLoopGroup();
    }

    @Bean
    public CuratorFramework curatorFramework(){
        CuratorFramework client = CuratorUtils.createClient(CuratorUtils.CONNECTION_STRING);
        client.start();
        return client;
    }

    @Bean
    public Serializer serializer(){
        return new HessianSerializer();
    }
}
