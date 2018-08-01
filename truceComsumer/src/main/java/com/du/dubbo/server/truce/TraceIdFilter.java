package com.du.dubbo.server.truce;

import com.alibaba.dubbo.rpc.*;

/**
 * @Author: duhongjiang
 * @Date: Created in 2018/7/31
 */
public class TraceIdFilter implements Filter {
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
        // *) 实际的rpc调用
        return invoker.invoke(invocation);
    }
}
