package com.du.dubbo.server.truce;

import com.alibaba.dubbo.rpc.RpcContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @Author: duhongjiang
 * @Date: Created in 2018/7/11
 */
public class DubboTruceTast {

    static ApplicationContext context;

    @Before
    public void setup() throws Exception {
        //String[] springConfig = {"applicationContext.xml"};
        context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    }

    @Test
    public void functionalTest() throws Exception {

//        RpcContext.getContext().setAttachment("traceId", "100001");
        System.out.println(RpcContext.getContext().getAttachments());
        SayHello sayHello = (SayHello) context.getBean("sayHelloImpl");
        sayHello.say("dujiang",24);


//        sayHello.say();



    }

    public static void main(String []args){
        try {
            DubboTruceTast dubboTruceTast =new DubboTruceTast();
            dubboTruceTast.setup();
            System.out.println(RpcContext.getContext().getAttachments());
            SayHello sayHello = (SayHello) context.getBean("sayHelloImpl");
//            sayHello.say("dujiang",24);
            Map info = new HashMap();
            info.put("name","dujiang");
            info.put("age",25);
//            sayHello.say("dujiang",24);
            sayHello.sayHello(info);
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
