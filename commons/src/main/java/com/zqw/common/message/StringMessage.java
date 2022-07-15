package com.zqw.common.message;


import lombok.Data;

/**
 * 字符串消息，简单的字符串消息
 */
@Data
public class    StringMessage extends Message {
    //传递的字符串类型
    private String str;

    //构造函数
    public StringMessage(String str) {
        super();
        this.str = str;
    }


    @Override
    public int getMessageType() {
        return 0;
    }

}
