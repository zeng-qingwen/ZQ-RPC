package com.zqw.common.serial;

import java.io.IOException;

/**
 * 序列化对象的接口
 */
public interface Serializer {

    /**
     * 将对象序列化为字节数组
     * @param obj 要序列化的对象
     * @return 序列化的字节数组
     * @throws IOException IO异常
     */
    byte[] serialize(Object obj) throws IOException;

    /**
     * 字节数组反序列化为指定对象
     * @param bytes 字节数组
     * @param tClass 对象类型
     * @param <T> 泛型
     * @return 反序列化后的对象
     * @throws IOException IO异常
     * @throws ClassNotFoundException 类不存在异常
     */

    <T> T deserialize(byte[] bytes,Class<T> tClass) throws IOException,ClassNotFoundException;



    /**
     * 以对象T为模板，序列化得到新的T
     * @param bytes 字节数组
     * @param t 对象T
     * @param <T> 泛型
     * @return 反序列化的结果
     * @throws ClassNotFoundException 类不存在
     * @throws IOException IO异常
     */
    default  <T> T deserialize(byte[] bytes,T t) throws ClassNotFoundException, IOException{
        return (T) deserialize(bytes,t.getClass());
    }

}
