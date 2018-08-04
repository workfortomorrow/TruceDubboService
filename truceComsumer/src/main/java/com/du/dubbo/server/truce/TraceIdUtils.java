package com.du.dubbo.server.truce;

/**
 * @Author: duhongjiang
 * @Date: Created in 2018/7/31
 */
public class TraceIdUtils {
    private static final ThreadLocal<String> TRACE_ID_CACHE
            = new ThreadLocal<String>();

    public static String getTraceId() {
        return TRACE_ID_CACHE.get();
    }

    public static void setTraceId(String traceId) {
        TRACE_ID_CACHE.set(traceId);
    }

    public static void clear() {
        TRACE_ID_CACHE.remove();
    }
}
