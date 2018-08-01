package com.du.dubbo.server.truce;

import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * @Author: duhongjiang
 * @Date: Created in 2018/7/31
 */
public class TraceIdFilter implements Filter {

//    private static Logger log =  LoggerFactory.getLogger(TraceIdFilter.class);
    Logger logger = LoggerFactory.getLogger("service");

//    private static Logger log =  Logger.getLogger(TraceIdFilter.class);

    public static void filter(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            // throw new IllegalArgumentException("The map is null!");
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
    public  Map<String, Object> getMap(Object[] objs,Class<?>[] parameterTypes){
//        Object[] objs = inv.getArguments();
//        Class<?>[] parameterTypes = inv.getParameterTypes();

        // 因运行平台暂只支持对Map的处理，故对请求参数都转为Map
        Map<String, Object> paramMap = new HashMap<String, Object>();
        try {
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> cla = parameterTypes[i];
                if (cla.isPrimitive()) {// 参数类型为基本类型
                    if (objs[i] != null) {
                        String parameterType = cla.getSimpleName();
                        String key = parameterType + i;
                        paramMap.put(key, objs[i]);
                    }
                } else if (cla.equals(Map.class)) {// Map类型的处理
                    paramMap.putAll((Map) objs[i]);
                    Map<String, Object> ma = new HashMap<>();
                    ma = paramMap;
                    filter(ma);
                    paramMap = ma;
                } else {// Bean对象的处理
//                    Map singleArg = PropertyUtils.describe(objs[i]);
                    paramMap.putAll((Map) objs[i]);
                }
            }
            return paramMap;
        } catch (Exception e) {
            String msg = "Failed to process request parameters and parameter types. message:" + e.getMessage();
            throw e;
        }
    }


    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = RpcContext.getContext().getAttachment("traceId");

        if ( traceId!=null &&  !"".equals(traceId) ) {
            // *) 从RpcContext里获取traceId并保存
            TraceIdUtils.setTraceId(traceId);
        } else {
            // *) 交互前重新设置traceId, 避免信息丢失
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
           // logger.info("method:["+invocation.getMethodName() +"],request:"+invocation.getArguments()+",takeTime:"+takeTime+" ms");
            Object o[] =invocation.getArguments();
//            if(o instanceof String) {
//                System.out.println(o.toString());
//            }

            System.out.println("Arguments :"+invocation.getArguments());
            System.out.println("MethodName :"+invocation.getMethodName());


//            Map<String, Object> paramMap = getMap(invocation.getArguments(),invocation.getParameterTypes());
//            System.out.println("arguments  :"+paramMap.toString());

        }

        return result;
    }
}
