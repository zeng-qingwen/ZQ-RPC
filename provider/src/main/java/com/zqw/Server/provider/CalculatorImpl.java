package com.zqw.Server.provider;

import com.zqw.Calculator;
import com.zqw.common.annotation.Provider;


@Provider
public class CalculatorImpl implements Calculator {

    @Override
    public int add(int a, int b) {
        return a+b;
    }

    @Override
    public int subtraction(int a, int b) {
        return a-b;
    }

    @Override
    public int multiple(int a, int b) {
        return a*b;
    }

    @Override
    public int division(int a, int b) {
        return a/b;
    }

}
