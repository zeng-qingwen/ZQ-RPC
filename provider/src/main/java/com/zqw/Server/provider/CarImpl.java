package com.zqw.Server.provider;


import com.zqw.Car;
import com.zqw.Server.annotation.Provider;

@Provider
public class CarImpl implements Car {

    @Override
    public String run() {
        return "小车车可以跑";
    }

}
