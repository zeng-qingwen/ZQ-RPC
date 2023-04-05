package com.zqw.Server.nettyserver;

import com.zqw.common.codec.MessageCodec;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Server {

    //开启服务端
    @Bean
    public  Channel start(NioEventLoopGroup boss, NioEventLoopGroup worker, ServerHandler serverHandler){
        Channel channel = null;
        try{
            channel = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new MessageCodec());
                            channel.pipeline().addLast(serverHandler);
                        }
                    }).bind(9095)
                    .sync()
                    .channel();
            return channel;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
