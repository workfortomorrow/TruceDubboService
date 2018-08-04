package com.du.dubbo.server.truce;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: duhongjiang
 * @Date: Created in 2018/7/30
 */
@Component
public class SayHelloImpl implements SayHello {

    @Resource
    ApplicationContext context;
    @Override
    public void say(String name,int age) {
        String traceId = TraceIdUtils.getTraceId();

        System.out.println("hello  :"+name+" your age :"+age+" traceId is: "+traceId);
        SayGoodBye sayGoodBye = (SayGoodBye) context.getBean("sayGoodByeImpl");

        sayGoodBye.say(name,age);

    }
}
