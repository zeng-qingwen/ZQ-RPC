package com.zqw.client;

import com.zqw.client.test.TestUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class);
        TestUtils testUtils = context.getBean(TestUtils.class);
        testUtils.testFunc();
        context.close();
    }

}
