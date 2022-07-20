# 项目名称ZQ-RPC

## 使用到的技术栈
技术栈: spring，springboot，netty，zookeeper，java反射，java动态代理，juc并发框架


## 项目亮点:
    1.自定义注解实现RPC微服务远程调用，无缝整合spring框架，提供开箱即用的功能，代码无侵入；
    2.采用反射和动态代理等设计模式对框架进行优化，大幅提高代码的可读性和扩展性，易维护；
    3.基于netty框架开发，灵活运用JUC框架，提升高并发效率，减少资源占用,整体提高系统30%QPS；
    4.加入zookeeper作为服务发现以及注册中心，提升整体系统架构的高可用，方便系统水平扩展；
    5.不断对项目架构进行重构升级，并在GitHub上开源，不断完善项目文档，规范用户使用手册；

## 基本功能概述
实现分布式RPC服务远程调用功能，提供负载均衡功能实现水平扩展

## 基本架构
![](https://raw.githubusercontent.com/zeng-qingwen/ZQ-RPC/master/img/%E6%95%B4%E4%BD%93%E6%9E%B6%E6%9E%84.png)
