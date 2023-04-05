package com.zqw.Server.annotation;


import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 注解标注服务提供者
 * 自定义注解provider服务提供者需要实现对应的服务接口，在相关的实现类中的类名上标注该方法，在容器启动的时候，
 * 会根据这个注解将这些实现类从ioc容器中挑选出来，然后注入到一个session对象中进行存储。
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Provider {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
