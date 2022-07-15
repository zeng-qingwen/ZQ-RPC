package com.zqw.Server.nettyserver;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress localHost = Inet4Address.getLocalHost();
        String hostAddress = localHost.getHostAddress();
        System.out.println(hostAddress);
    }

}
