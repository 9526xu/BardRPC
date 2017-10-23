package com.bard.rpc;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/23
 * rpc cliet 接口定义
 */
public abstract class RpcClient {


    protected RpcConnectConfig config;

    protected String host;

    protected int port;


    public RpcClient(String host, int port) {
        this(new RpcConnectConfig(host, port));
    }

    public RpcClient(RpcConnectConfig config) {
        this.config = config;
    }


    /**
     * 负责导入（import）远程接口的代理实现
     *
     * @param classT
     * @param <T>
     * @return
     */
    public abstract <T> T refer(Class<T> classT);

}
