package com.zqw.Server.nettyserver;

import com.zqw.common.message.RpcRequestMessage;
import com.zqw.common.message.RpcResponseMessage;
import com.zqw.common.message.StringMessage;
import com.zqw.common.message.StringsMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * netty的一个处理请求的handler类
 * 用来区分不同的前端请求
 */

@Component
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter{

    public ProviderMap providerSession;
    private String[] arr = null;

    public ServerHandler(ProviderMap providerSession) {
        this.providerSession = providerSession;
        Set<String> stringSet = providerSession.providerMap.keySet();
        this.arr = new String[stringSet.size()];
        stringSet.toArray(arr);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        if (msg instanceof StringMessage) {//如果是String请求
            dealWhitStringRequest(ctx,(StringMessage) msg);
        } else if (msg instanceof RpcRequestMessage) {  //如果是RPC请求
            dealWithRpcRequest(ctx,(RpcRequestMessage) msg);
        }
    }


    //处理String请求
    public void dealWhitStringRequest(ChannelHandlerContext ctx,StringMessage message){
        System.out.println(message);
        if(message.getStr().equals("onLine")){
            StringsMessage stringsMessage = new StringsMessage(arr);
            stringsMessage.setSequenceId(message.getSequenceId());
            ctx.writeAndFlush(stringsMessage);
        }
    }



    //处理rpc请求
    public void dealWithRpcRequest(ChannelHandlerContext ctx,RpcRequestMessage message){
        System.out.println(message);
        String interfaceName = message.getInterfaceName();      //接口名
        String methodName = message.getMethodName();            //方法名
        Class<?> returnType = message.getReturnType();          //返回类型
        Class[] parameterTypes = message.getParameterTypes();   //参数类型数组
        Object[] parameterValue = message.getParameterValue();  //参数值数组
        Object obj =  this.providerSession.getInstance(interfaceName);
        Class<?> aClass = obj.getClass();
        RpcResponseMessage rpcResponseMessage = new RpcResponseMessage();
        try {
            Method method = aClass.getMethod(methodName, parameterTypes);
            Object invoke = method.invoke(obj, parameterValue);
            rpcResponseMessage.setSequenceId(message.getSequenceId());
            rpcResponseMessage.setReturnValue(invoke);
            ctx.writeAndFlush(rpcResponseMessage);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            rpcResponseMessage.setExceptionValue(e);
            ctx.writeAndFlush(rpcResponseMessage);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
