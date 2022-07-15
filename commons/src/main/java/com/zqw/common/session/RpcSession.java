package com.zqw.common.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class RpcSession {

    //map属性
    public  Map<Integer,Object> sessionMap = new HashMap<>();

    //添加等待
    public void addWait(int serialId, CountDownLatch countDownLatch){
        sessionMap.put(serialId,countDownLatch);
    }

    //添加返回结果
    public void addResult(int serialId,Object ans){
        CountDownLatch countDownLatch = (CountDownLatch) sessionMap.get(serialId);
        sessionMap.put(serialId,ans);
        countDownLatch.countDown();
    }

    //取结果并且清除map中的记录
    public Object getResult(int serialId){
        Object ans = sessionMap.get(serialId);
        sessionMap.remove(serialId);
        return ans;
    }

    //获取除sessionMap keySet以外的一个随机值
    public int getSerialId(){
        Random random = new Random();
        int ans = random.nextInt();
        while (sessionMap.keySet().contains(ans)){
            ans = random.nextInt();
        }
        return  ans;
    }
}
