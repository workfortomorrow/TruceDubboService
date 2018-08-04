package com.du.dubbo.server.truce;

import org.springframework.stereotype.Component;

/**
 * @Author: duhongjiang
 * @Date: Created in 2018/7/31
 */
@Component
public class SayGoodByeImpl implements SayGoodBye {
    @Override
    public void say(String name,int age) {
        String traceId = TraceIdUtils.getTraceId();

        System.out.println("goodbye  name: "+name+" age is :"+age+" traceId :"+traceId);
    }
}
