package com.zqw.Server.provider;



import com.zqw.Service;
import com.zqw.common.annotation.Provider;

@Provider
public class ServiceImpl implements Service {
    @Override
    public String hello(String str) {
        return "hello "+str;
    }
}
