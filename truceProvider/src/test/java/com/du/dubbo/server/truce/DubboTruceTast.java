package com.du.dubbo.server.truce;

import com.github.kristofa.brave.dubbo.support.DefaultClientNameProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 测试 spring batch框架方式集成交易数据
 * @Author: duhongjiang
 * @Date: Created in 2018/7/11
 */
public class DubboTruceTast {

    ApplicationContext context;

    @Before
    public void setup() throws Exception {
        //String[] springConfig = {"applicationContext.xml"};
        context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    }

    @Test
    public void functionalTest() throws Exception {

        SayHello sayHello = (SayHello) context.getBean("sayHelloImpl");
        sayHello.say("duhj",23);
    }
    public static void main(String []args){
        try {
            DubboTruceTast dubboTruceTast=new DubboTruceTast();
            dubboTruceTast.setup();
            System.in.read();

        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuffer strinBuffer;
        ConcurrentHashMap map;
        DefaultClientNameProvider ee;


    }
}
