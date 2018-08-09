# Dubbo服务调用链追踪

#### 大体以下几种

- Google Dapper 分布式追踪系统
- 拦截请求  zipkin(存储 in-memory,mysql,cassandra,elasticsearch)
- 字节码注入实现 pinpoint(存储 HBase)  skywalking(存储 elasticsearch，h2)

### TruceDubboService 使用zipkin实现

- 服务调用链分析(时长)
- 服务依赖分析
