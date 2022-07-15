package com.zqw.Server.nettyserver;

import com.zqw.common.annotation.Provider;
import com.zqw.common.serial.Serializer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;


@Component
public class ProviderMap implements BeanFactoryAware {

    @Autowired
    Serializer serializer;

    @Autowired
    public CuratorFramework curatorFramework;

    public Map<String,Object> providerMap = new HashMap<>();


    public  void fillMap(Object ... o){
        int length = o.length;
        Class[] arr = new Class[length];
        for (int i = 0; i < length; i++) {
            //按照对象的接口名为key对象的数据为value，对map进行填充
            for (Class<?> anInterface : o[i].getClass().getInterfaces()) {
                System.out.println(anInterface);
                providerMap.put(anInterface.getName(),o[i]);
            }
        }
    }

    public  Object getInstance(String key){
        return providerMap.get(key);
    }

    public <T> T getInstance(String key,Class<T> tClass){
        return (T)getInstance(key);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if(beanFactory instanceof ConfigurableListableBeanFactory){
            //先根据注解拿到所有的供应者类，将供应者类注入到session中
            ConfigurableListableBeanFactory configurableListableBeanFactory = (ConfigurableListableBeanFactory) beanFactory;
            Map<String, Object> annotation = configurableListableBeanFactory.getBeansWithAnnotation(Provider.class);
            Collection<Object> values = annotation.values();
            Object[] objects = values.toArray();
            fillMap(objects);
        }
        registerInterfaces();
    }

    /**
     * 将所有的服务接口注册到zookeeper上，如果已经存在了就对比本项目中的接口名和
     * 创建interface-- > ip:port   的map映射 并将该对象序列化后注册到zookeeper上
     */
    public void registerInterfaces(){

//        解析interface接口集合
        Set<String> interfaceNames = providerMap.keySet();
        String zkPath = "/ZQRPC/interfaces";

//        解析本地ip拼接端口
        String hostAddress =":8081";
        try {
            InetAddress localHost = Inet4Address.getLocalHost();
            hostAddress = localHost.getHostAddress()+hostAddress;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

//        注册到zookeeper上
        try {
                
            Stat stat = curatorFramework.checkExists().forPath(zkPath);

            if(null == stat){ //如果是第一个注册的节点就创建节点，并添加接口信息到zookeeper上

                Map<String, List<String>> interfaceIpMap = new HashMap<>();
                for (String interfaceName : interfaceNames) {
                    List<String> iPList = null;
                    if(interfaceIpMap.containsKey(interfaceName)){
                        iPList = interfaceIpMap.get(interfaceName);
                    }else{
                        iPList = new ArrayList<>();
                    }
                    iPList.add(hostAddress);
                    interfaceIpMap.put(interfaceName,iPList);
                }
                byte[] serialize = serializer.serialize(interfaceIpMap);
                Map<String, List<String>> map = serializer.deserialize(serialize,Map.class);

            }else{//如果不是第一个添加的节点，就拉取下其他节点注册的结果然后再添加上这个节点上实现了的ip上

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
