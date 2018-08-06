package com.du.dubbo.server.truce;

import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.alibaba.dubbo.common.extension.Activate;
import java.util.concurrent.ConcurrentMap;


/**
 * @Author: duhongjiang
 * @Date: Created in 2018/7/31
 */
@Activate(group = "provider")
public class TraceIdFilter implements Filter {

    Logger logger = LoggerFactory.getLogger("service");


    /**
     *  对Dubbo请求参数封装map
     * @param objs
     * @param parameterTypes
     * @return
     *
     * 交互前重新设置traceId, 避免信息丢失
     */
    public  Map<String, Object> getMap(Object[] objs,Class<?>[] parameterTypes){
//        Map<String, Object> paramMap = new HashMap<String, Object>(16);
        Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>(20);


        try {
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> cla = parameterTypes[i];
                /**
                 * 参数类型为基本类型
                 * Map类型的处理
                 * Bean对象的处理
                  */
                if (cla.isPrimitive()) {
                    if (objs[i] != null) {
                        String parameterType = cla.getSimpleName();
                        String key = parameterType + i;
                        paramMap.put(key, objs[i]);
                    }
                }else if(cla.equals(String.class)) {
                    paramMap.put(cla.toString(),(String)objs[i]);
                }else if(cla.equals(Integer.class)){
                    paramMap.put(cla.toString(),(Integer)objs[i]);
                }else if (cla.equals(Map.class)) {
                    paramMap.putAll((Map) objs[i]);
                    Map<String, Object> ma = new HashMap<>(20);
//                    Map<String, Object> ma = new ConcurrentHashMap<>(20);
                    ma = paramMap;
                    filter(ma);
                    paramMap = ma;
                } else {
                    Map singleArg = PropertyUtils.describe(objs[i]);
                    paramMap.putAll((Map) objs[i]);
                }
            }

        } catch (Exception e) {
            String msg = "Failed to process request parameters and parameter types. message:" + e.getMessage();

        }
        return paramMap;
    }

    /**
     * 对 参数Map进行迭代筛除 class类型参数
     * @param map
     */
    public static void filter(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        for (String key : map.keySet()) {
            if ("class".equals(key)) {
                map.remove(key);
                break;
            }
            if (Map.class.isInstance(map.get(key))) {
                filter((Map<String, Object>) map.get(key));
            }
            if (List.class.isInstance(map.get(key))) {
                for (Object obj : (List) map.get(key)) {
                    if (Map.class.isInstance(obj)) {
                        filter((Map<String, Object>) obj);
                    }
                }
            }
        }
    }

    /**
     * 动态代理 调用
     * @param invoker
     * @param invocation
     * @return
     * @throws RpcException
     *
     * 1.从RpcContext里获取traceId并保存
     * 2.交互前重新设置traceId, 避免信息丢失
     * 3.对调用 处理追踪日志
     * 4.真实的调用，并返回结果
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = RpcContext.getContext().getAttachment("traceId");

        if ( traceId!=null &&  !"".equals(traceId) ) {
            TraceIdUtils.setTraceId(traceId);
        } else {
            RpcContext.getContext().setAttachment("traceId", TraceIdUtils.getTraceId());
        }
        System.out.println("traceId :"+RpcContext.getContext().getAttachment("traceId"));
        System.out.println("Url :"+RpcContext.getContext().getUrl());
        System.out.println("MethodName :"+RpcContext.getContext().getMethodName());
        System.out.println("LocalAddress :"+RpcContext.getContext().getLocalAddressString());
        System.out.println("localHost :"+RpcContext.getContext().getLocalHost());
        System.out.println("localPort :"+RpcContext.getContext().getLocalPort());
        System.out.println("remoteAddress :"+RpcContext.getContext().getRemoteAddressString());
        System.out.println("RemoteHost :"+RpcContext.getContext().getRemoteHost());
        System.out.println("RemotePort :"+RpcContext.getContext().getRemotePort());
        logger.debug("traceId"+traceId);
        // *) 实际的rpc调用
        Result result=null;
        Long takeTime = 0L;
        try {
            Long startTime = System.currentTimeMillis();
            result = invoker.invoke(invocation);
            takeTime = System.currentTimeMillis() - startTime;
            System.out.println("takeTime :"+takeTime);
        }catch (Exception e){

        }finally {
            logger.info("method:["+invocation.getMethodName() +"],request:"+invocation.getArguments()+",takeTime:"+takeTime+" ms");
            Object [] o =invocation.getArguments();
            for (Object i:o){
                if(i instanceof String) {
                    System.out.println("name: "+(String)i.toString());
                }else if(i instanceof Integer){
                    System.out.println("age: "+((Integer) i).doubleValue());
                }
            }
            Map<String, Object> parameter = getMap(o,invocation.getParameterTypes());
            System.out.println("all parameter : :"+parameter.toString());
            System.out.println("Arguments :"+invocation.getArguments());
            System.out.println("MethodName :"+invocation.getMethodName());

        }

        return result;
    }
}
