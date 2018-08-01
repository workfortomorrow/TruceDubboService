package com.du.dubbo.server.truce;

import org.springframework.stereotype.Component;

/**
 * @Author: duhongjiang
 * @Date: Created in 2018/7/31
 */
@Component
public class SayGoodByeImpl implements SayGoodBye {
    @Override
    public void say() {
        String traceId = TraceIdUtils.getTraceId();

        System.out.println("goodbye  "+traceId);
    }
}
