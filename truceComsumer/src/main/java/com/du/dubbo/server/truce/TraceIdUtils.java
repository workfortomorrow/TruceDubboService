package com.du.dubbo.server.truce;

/**
 * @Author: duhongjiang
 * @Date: Created in 2018/7/31
 */
public class TraceIdUtils {
    private static final ThreadLocal<String> traceIdCache
            = new ThreadLocal<String>();

    public static String getTraceId() {
        return traceIdCache.get();
    }

    public static void setTraceId(String traceId) {
        traceIdCache.set(traceId);
    }

    public static void clear() {
        traceIdCache.remove();
    }
}
