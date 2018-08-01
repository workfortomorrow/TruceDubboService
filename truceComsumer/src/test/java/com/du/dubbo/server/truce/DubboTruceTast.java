package com.du.dubbo.server.truce;

import com.alibaba.dubbo.rpc.RpcContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

        RpcContext.getContext().setAttachment("traceId", "100001");
        System.out.println(RpcContext.getContext().getAttachments());
        SayHello sayHello = (SayHello) context.getBean("sayHelloImpl");
        sayHello.say("dujiang");


//        sayHello.say();


        System.in.read();
    }
}
