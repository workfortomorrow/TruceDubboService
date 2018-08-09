package com.du.dubbo.server.truce;

import java.util.Map;

/**
 * @Author: duhongjiang
 * @Date: Created in 2018/7/30
 */
public interface SayHello {
    /**
     * test
     * @param name
     * @param age
     */
    public void say(String name,int age);

    /**
     *
     * @param info
     */
    public void sayHello(Map info);

}
