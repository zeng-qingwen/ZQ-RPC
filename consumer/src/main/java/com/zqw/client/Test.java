package com.zqw.client;

import com.zqw.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class Test {


    public UserMapper userMapper;

    public Test(UserMapper userMapper) {
        this.userMapper = userMapper;
        for (int i = 0; i < 20; i++) {
            System.out.println(userMapper.getUser(i));
        }
    }
}
