package com.bard.rpc;

import lombok.Data;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/23
 * 负责组装连接信息等参数
 */
@Data
public class RpcConnectConfig {

    private String host;

    private String port;
    /**
     * 默认超时60s
     */
    private Long connectTimeOut = DEFAULT_TIME_OUT;

    private static final Long DEFAULT_TIME_OUT = 60000L;


    public RpcConnectConfig(String host, String port, Long connectTimeOut) {
        this.host = host;
        this.port = port;
        this.connectTimeOut = connectTimeOut;
    }

    public RpcConnectConfig(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public RpcConnectConfig() {
    }
}

