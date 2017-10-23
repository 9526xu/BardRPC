package com.bard.rpc.impl;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/23
 * rpc cliet 接口定义
 */
public interface RpcClient {
    /**
     * 负责导入（import）远程接口的代理实现
     * @param classT
     * @param <T>
     * @return
     */
    <T> T refer(Class<T> classT);
}
