package com.du.dubbo.server.truce;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.github.kristofa.brave.dubbo.BraveConsumerFilter;
import com.github.kristofa.brave.dubbo.BraveProviderFilter;

/**
 * @Author: duhongjiang
 * @Date: Created in 2018/7/31
 */
//@Activate(group = "consumer")
@Activate(group = "provider")
public class TraceIdFilter implements Filter {

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
            // *) 从RpcContext里获取traceId并保存
            TraceIdUtils.setTraceId(traceId);
        } else {
            // *) 交互前重新设置traceId, 避免信息丢失
            RpcContext.getContext().setAttachment("traceId", TraceIdUtils.getTraceId());
        }
        System.out.println("########################################");
        System.out.println("traceId :"+RpcContext.getContext().getAttachment("traceId"));
        System.out.println("Url :"+RpcContext.getContext().getUrl());
        System.out.println("MethodName :"+RpcContext.getContext().getMethodName());
        System.out.println("LocalAddress :"+RpcContext.getContext().getLocalAddressString());
        System.out.println("localHost :"+RpcContext.getContext().getLocalHost());
        System.out.println("localPort :"+RpcContext.getContext().getLocalPort());
        System.out.println("remoteAddress :"+RpcContext.getContext().getRemoteAddressString());
        System.out.println("RemoteHost :"+RpcContext.getContext().getRemoteHost());
        System.out.println("RemotePort :"+RpcContext.getContext().getRemotePort());
        System.out.println("ParameterTypes :"+RpcContext.getContext().getParameterTypes());
        System.out.println("Arguments :"+invocation.getArguments());
        System.out.println("Attachments :"+invocation.getAttachments().toString());
        System.out.println("Attachments :"+invocation.getParameterTypes().toString());
        System.out.println("Parameters :"+RpcContext.getContext().getUrl().getParameters());
        System.out.println("MethodParameter name :"+RpcContext.getContext().getUrl().getMethodParameter(invocation.getMethodName(),"name"));
        System.out.println("MethodParameter age :"+RpcContext.getContext().getUrl().getMethodParameter(invocation.getMethodName(),"age"));
        System.out.println("########################################");

        // *) 实际的rpc调用
        return invoker.invoke(invocation);
    }
}
