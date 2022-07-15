package com.zqw.Server.provider;


import com.zqw.UserMapper;
import com.zqw.common.annotation.Provider;

@Provider
public class UserMapperImpl implements UserMapper {

    @Override
    public String getUser(int id) {
        return "第"+id+"号用户";
    }

}
