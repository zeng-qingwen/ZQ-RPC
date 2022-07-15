package com.zqw.common.codec;



import com.zqw.common.message.Message;
import com.zqw.common.serial.HessianSerializer;
import com.zqw.common.serial.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.Data;

import java.util.List;

/**
 * 消息编解码
 * 将消息编码成字符串传输方便传输到网络上进行传输
 * 将网络上的二进制字节解码成消息对象
 */
@Data
public class MessageCodec extends ByteToMessageCodec<Message> {

    Serializer serializer = new HessianSerializer();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        //序列化对象
        byte[] bytes = serializer.serialize(message);

        //获取字节数组的长度，然后再写入到buf中传输到网络上
        int length = bytes.length;
        byteBuf.writeInt(length);

        //将序列化后的字节数组写入到buf中
        byteBuf.writeBytes(bytes);
    }


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //长度
        int length = byteBuf.getInt(0);

        //将有用的消息截取出来
        byte[] bytes = new byte[length];
        byteBuf.getBytes(4,bytes);
        byteBuf.skipBytes(byteBuf.readableBytes());

        //将消息反序列化
        Message message = serializer.deserialize(bytes,Message.class);
        list.add(message);
    }

}
