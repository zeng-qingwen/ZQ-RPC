package com.zqw.Server.provider;



import com.zqw.HelloService;
import com.zqw.Server.annotation.Provider;

@Provider
public class ServiceImpl implements HelloService {
    @Override
    public String hello(String str) {
        return "hello "+str;
    }
}
