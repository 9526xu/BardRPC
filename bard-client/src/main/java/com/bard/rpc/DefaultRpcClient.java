package com.bard.rpc;

import java.lang.reflect.Proxy;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/23
 * RpcClient 默认实现
 */
public class DefaultRpcClient extends RpcClient {


    public DefaultRpcClient(String host, String port) {
        super(host, port);
    }

    @Override
    public <T> T refer(Class<T> classT) {
        // 代理生成实现类
        T result = (T) Proxy.newProxyInstance(DefaultRpcClient.class.getClassLoader(), new Class[]{classT}, new RpcInvocationHandler(this.config));
        return result;
    }
}
