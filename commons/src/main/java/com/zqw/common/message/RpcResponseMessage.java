package com.zqw.common.message;

import lombok.Data;
import lombok.ToString;

/**
 * rpc回应请求的消息类
 */
@Data
@ToString(callSuper = true)
public class RpcResponseMessage extends Message {

    //返回值
    private Object returnValue;

    //异常值
    private Exception exceptionValue;

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_RESPONSE;
    }

}
