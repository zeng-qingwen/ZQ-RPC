package com.zqw.common.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * rpc消息传递的抽象类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Message implements Serializable {

    //消息的序列号
    private int sequenceId;

    //消息的类型
    private int messageType;

    //消息请求静态属性
    public static final int RPC_MESSAGE_TYPE_REQUEST = 101;

    //消息回应静态属性
    public static final int  RPC_MESSAGE_TYPE_RESPONSE = 102;

    //获取消息类型的抽象方法
    public abstract int getMessageType();

}
