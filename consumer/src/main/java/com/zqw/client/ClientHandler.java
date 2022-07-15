package com.zqw.client;


import com.zqw.common.message.Message;
import com.zqw.common.message.RpcResponseMessage;
import com.zqw.common.message.StringsMessage;
import com.zqw.common.session.RpcSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 处理客户端接收到服务端消息时做出的相关逻辑
 */
@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<Message> {

    public RpcSession rpcSession;

    public ClientHandler(RpcSession rpcSession) {
        this.rpcSession = rpcSession;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        if(message instanceof RpcResponseMessage){
            rpcSession.addResult(message.getSequenceId(),message);
        }if(message instanceof StringsMessage){
            StringsMessage StringsMessage = (StringsMessage)message;
            rpcSession.addResult(StringsMessage.getSequenceId(),StringsMessage.getStrings());
        }
    }

}