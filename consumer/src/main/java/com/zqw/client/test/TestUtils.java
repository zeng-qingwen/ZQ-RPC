package com.zqw.client.test;

import com.zqw.Calculator;
import com.zqw.Car;
import com.zqw.HelloService;
import com.zqw.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class TestUtils {
    @Autowired
    public HelloService helloService;
    @Autowired
    public Calculator calculator;
    @Autowired
    public Car car;
    @Autowired
    public UserMapper userMapper;

    public void testFunc() {
        String cmd;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                cmd = scanner.nextLine();
                System.out.println(cmd);
                if ("exit".equals(cmd)) break;
                else if ("car".equals(cmd)) {
                    System.out.println(car.run());
                } else if (cmd.equals("calc")) {
                    System.out.println("\n测试Calculator接口的功能\n请输入两个整数");
                    int a = scanner.nextInt();
                    int b = scanner.nextInt();
                    System.out.println(calculator.add(a, b));
                } else if (cmd.equals("hello")) {
                    System.out.println("\n测试HelloService接口的功能\n请输入一段字符串");
                    String data = scanner.nextLine();
                    System.out.println(helloService.hello(data));
                } else if (cmd.equals("user")) {
                    System.out.println("\n测试UserMapper接口的功能\n请输入一个userId");
                    int Id = scanner.nextInt();
                    System.out.println(userMapper.getUser(Id));
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
