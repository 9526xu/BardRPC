# BardRPC
分布式 RPC框架 用于熟悉 RPC框架的原理
## 项目框架
采用[netty](https://github.com/netty/netty/)进行网络通讯,序列化框架采用[Kryo](https://github.com/EsotericSoftware/kryo)。

## 实现功能
- [ x ] 基本的 RPC 功能实现,客户端远程调用服务端服务。
- [ ] 服务端使用线程池,超时控制
- [ ] 异步调用实现
- [ ] 服务注册与发现 Zookeeper,Eureka作为服务中心
- [ ] Spring集成
- [ ] 注解服务发布，调用
- [ ] Spring boot 模块包


