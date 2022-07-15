package com.zqw.client;

import com.zqw.Calculator;
import com.zqw.Car;
import com.zqw.Service;
import com.zqw.common.session.RpcSession;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class TestUtils {

    @Autowired
    public Channel channel;

    @Autowired
    public RpcSession rpcSession;

    @Autowired
    public NioEventLoopGroup group;

    @Autowired
    public Service service1;

    @Autowired
    public Calculator calculator;

    @Autowired
    public Car car;

    public void dealWithRpc(){
        new Thread(()->{

            String cmd = null;

            Scanner scanner = new Scanner(System.in);

            for (int i = 0; i < 6; i++) {
                System.out.println(calculator.add(i, i + 1));
            }

            while (true) {
                cmd = scanner.nextLine();
                System.out.println(cmd);
                if (cmd != null && "exit".equals(cmd)) break;
                else if (cmd != null && "car".equals(cmd)) System.out.println(car.run());
                else if(cmd.equals("calc")){
                    System.out.println("请输入两个整数");
                    int a = scanner.nextInt();
                    int b = scanner.nextInt();
                    System.out.println(calculator.add(a,b));
                }else if(cmd.equals("hello")){
                    System.out.println("请输入一段字符串");
                    String data = scanner.nextLine();
                    System.out.println(service1.hello(data));
                }
            }
//            try {
//                channel.close().sync();
//
//                Runtime.getRuntime().addShutdownHook(new Thread(() -> group.shutdownGracefully()));

//                group.next().submit(() -> {
//                    group.shutdownGracefully();
//                });

//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }).start();
    }
}
