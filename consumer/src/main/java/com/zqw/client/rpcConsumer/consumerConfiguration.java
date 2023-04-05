package com.zqw.client.rpcConsumer;

import com.zqw.common.codec.MessageCodec;
import com.zqw.common.message.StringMessage;
import com.zqw.client.rpcConsumer.proxy.JDKProxyBuilder;
import com.zqw.client.rpcConsumer.session.RpcSession;
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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;


@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class consumerConfiguration implements BeanFactoryPostProcessor {

    @Bean
    public InetSocketAddress remoteAddress(){
        InetAddress localHost = null;
        String hostAddress = "";
        try {
            localHost = Inet4Address.getLocalHost();
            hostAddress = localHost.getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println(e.getCause().getMessage());
        }
        return new InetSocketAddress(hostAddress, 9095);
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
            // 建立TCP Channel长连接
            Channel channel = bootstrap.connect(remoteAddress).sync().channel();

            // 向微服务提供者发送消息，新的consumer已经建立连接了,获取微服务提者实现的业务代码接口
            CountDownLatch countDownLatch = new CountDownLatch(1);
            int serialId = rpcSession.getSerialId();
            StringMessage msg = new StringMessage("onLine");
            msg.setSequenceId(serialId);
            channel.writeAndFlush(msg);
            rpcSession.addWait(serialId,countDownLatch);
            countDownLatch.await();
            JDKProxyBuilder jdkProxyBuilder = new JDKProxyBuilder(channel, rpcSession);
            String[] arr = (String[])rpcSession.getResult(serialId);

            for (String s : arr) {
                try {
                    Class<?> aClass = Class.forName(s);
                    System.out.println("注入了-->"+aClass+"  实例");
                    // JDK动态代理生成业务接口的代理类，并注入到IOC容器中
                    assert defaultListableBeanFactory != null;
                    Object JDKInstance = jdkProxyBuilder.getInstance(aClass);
                    defaultListableBeanFactory.registerSingleton(aClass.getSimpleName().toLowerCase(Locale.ROOT), JDKInstance);
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
