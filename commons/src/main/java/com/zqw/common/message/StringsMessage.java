package com.zqw.common.message;

/**
 * 字符串数组消息类
 * 用来传递一组字符串的消息
 */
public class StringsMessage extends Message{

    //字符串数组
    String[] strings;

    //构造函数
    public StringsMessage(String[] strings) {
        this.strings = strings;
    }


    public String[] getStrings() {
        return strings;
    }

    @Override
    public int getMessageType() {
        return 0;
    }

}
