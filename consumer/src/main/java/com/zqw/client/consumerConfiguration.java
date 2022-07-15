package com.zqw.client;

import com.zqw.common.codec.MessageCodec;
import com.zqw.common.message.StringMessage;
import com.zqw.common.proxy.JDKProxyBuilder;
import com.zqw.common.session.RpcSession;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.net.InetSocketAddress;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;


@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class consumerConfiguration implements BeanFactoryPostProcessor {

    @Bean
    public InetSocketAddress remoteAddress(){
        return new InetSocketAddress("192.168.43.149", 8081);
    }

    @Bean
    public NioEventLoopGroup group(){
        return  new NioEventLoopGroup(2);
    }

    @Bean
    RpcSession rpcSession(){
        return new RpcSession();
    }


    @Bean
    public Bootstrap bootstrap(ChannelHandler messageCodec, ChannelHandler clientHandler, EventLoopGroup group){
        return  new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {

                        channel.pipeline().addLast(messageCodec);

                        channel.pipeline().addLast(clientHandler);
                    }
                });
    }

    @Bean
    public ClientHandler clientHandler(RpcSession rpcSession){
        return  new ClientHandler(rpcSession);
    }

    @Bean
    MessageCodec messageCodec(){
        return new MessageCodec();
    }

    /**
     * 向容器中注入channel和rpc通信的信道
     * @param remoteAddress 服务端的地址
     * @param rpcSession rpc会话
     * @param bootstrap 启动类
     * @param beanFactory
     * @return
     */
    @Bean
    Channel channel(InetSocketAddress remoteAddress, RpcSession rpcSession, Bootstrap bootstrap, BeanFactory beanFactory) {
        DefaultListableBeanFactory defaultListableBeanFactory = null;
        if(beanFactory instanceof DefaultListableBeanFactory){
            defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
         }

        try {
            Channel channel = bootstrap.connect(remoteAddress).sync().channel();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            int serialId = rpcSession.getSerialId();
            StringMessage msg = new StringMessage("onLine");
            msg.setSequenceId(serialId);
            channel.writeAndFlush(msg);
            rpcSession.addWait(serialId,countDownLatch);
            countDownLatch.await();

            String[] arr = (String[])rpcSession.getResult(serialId);
            for (String s : arr) {
                try {
                    Class<?> aClass = Class.forName(s);
                    System.out.println("注入了-->"+aClass+"  实例");
                    defaultListableBeanFactory.registerSingleton(aClass.getSimpleName().toLowerCase(Locale.ROOT),new JDKProxyBuilder(channel,rpcSession).getInstance(aClass));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return channel;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        NioSocketChannel channel = configurableListableBeanFactory.getBean("channel", NioSocketChannel.class);
    }

}
