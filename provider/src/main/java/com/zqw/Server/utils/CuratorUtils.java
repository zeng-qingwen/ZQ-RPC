package com.zqw.Server.utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorUtils {

    public final static String CONNECTION_STRING = "127.0.0.1:2181";

    /**
     * 根据zookeeper地址和端口号创建CuratorFramework对象
     * @param connectionString 连接的地址和端口号
     * @return  创建的结果
     */
    public static CuratorFramework createClient(String connectionString){
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        return CuratorFrameworkFactory.newClient(connectionString,retry);
    }


    /**
     * 创建CuratorFramework
     * @param connectionString 连接地址
     * @param retry 重试机制
     * @param connectionTimeoutMs 连接超时时间
     * @param sessionTimeoutMs  session超时时间
     * @return CuratorFramework实例
     */
    public static CuratorFramework createClient(String connectionString,ExponentialBackoffRetry  retry,int connectionTimeoutMs,int sessionTimeoutMs){
        return CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .retryPolicy(retry)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }



}

