package com.zqw.common.serial;

import com.google.gson.Gson;
import io.netty.channel.ChannelHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@ChannelHandler.Sharable
public class GsonSerializer implements Serializer {

    public Gson gson;
    public GsonSerializer() {
        this.gson = new Gson();
    }

    @Override
    public byte[] serialize(Object obj) throws IOException {
        String json = gson.toJson(obj);
        return json.getBytes(StandardCharsets.UTF_8);
    }


    @Override
    public <T> T deserialize(byte[] bytes, Class<T> tClass) throws IOException, ClassNotFoundException {
        String json = new String(bytes, StandardCharsets.UTF_8);
        return gson.fromJson(json, tClass);
    }

}
