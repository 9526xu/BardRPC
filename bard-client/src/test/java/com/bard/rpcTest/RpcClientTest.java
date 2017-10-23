package com.bard.rpcTest;

import com.bard.rpc.DefaultRpcClient;
import com.bard.rpc.RpcClient;
import org.junit.Test;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/23
 */
public class RpcClientTest {
    @Test
    public void rpcClientTest() {
        RpcClient rpcClient = new DefaultRpcClient("127.0.0.1", 9876);
        HelloWorldInface inface = rpcClient.refer(HelloWorldInface.class);
        inface.helloWorld("hello RPC");
    }

}
