package com.zqw.common.proxy;


import com.zqw.common.message.RpcRequestMessage;
import com.zqw.common.message.RpcResponseMessage;
import com.zqw.common.session.RpcSession;
import io.netty.channel.Channel;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;



public class JDKProxyBuilder {

    //消息信道，用来和服务端通信
    private final Channel channel;

    //rpc请求的session会话
    private final RpcSession rpcSession;

    //构造函数，自动注入信道和session
    public JDKProxyBuilder(Channel channel, RpcSession rpcSession) {
        this.channel = channel;
        this.rpcSession = rpcSession;
    }

    //根据接口生成对应的代理类
    public  <T> T getInstance(T t){
        Class<?> Clazz = t.getClass();
        return (T)getInstance(Clazz);
    }

    //根据接口类 类型去获取代理类
    public   <T> T getInstance(Class<T> tClass){
        Assert.state(tClass.isInterface(),"这个类不是一个接口");
        return (T) Proxy.newProxyInstance(
                tClass.getClassLoader(),
                new Class[]{tClass},
                new Invoker());
    }


    /**
     * 代理接口中的所有方法，当调用代理的方法的时候不会在本地执行处理，而是会调调用远程的方法处理，
     * 然后获取相关的返回值，返回给调用方
     */
    class Invoker implements InvocationHandler{
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //生成序列号由rpcSession生成，可以保证不会产生冲突
            int serialId = rpcSession.getSerialId();

            //新建一个rpc消息，发送给后端
            RpcRequestMessage rpcRequestMessage = new RpcRequestMessage(
                    serialId,                                           //序列号
                    proxy.getClass().getInterfaces()[0].getName(),      //接口字
                    method.getName(),                                   //方法名
                    method.getReturnType(),                             //方法返回类型
                    method.getParameterTypes(),                         //方法参数类型
                    args                                                //方法参数
            );

            //发送消息
            channel.writeAndFlush(rpcRequestMessage);

            //新建一个CountDownLatch让该线程等待消息传递回来，将这个消息注册到rpcSession中
            //如果供应者返回的执行结果到了可以让接收消息的线程到rpcSession中去获取这个CountDownLatch
            //对象，将当前等待的线程唤醒，再将返回结果放入到session中，，当前线程唤醒之后再到rpcSession中
            //去获取结果，返回给调用者，其中如何准确、无误地唤醒线程、放入结果、拿到结果的关键点是serialId序列号
            //在rpcSession中有一个Map<Integer,Object>对象属性

            CountDownLatch countDownLatch = new CountDownLatch(1);
            rpcSession.addWait(serialId,countDownLatch);
            countDownLatch.await();

            RpcResponseMessage result = (RpcResponseMessage)rpcSession.getResult(serialId);

            return result.getReturnValue();
        }
    }

}
