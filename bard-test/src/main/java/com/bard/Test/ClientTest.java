package com.bard.Test;

import com.bard.rpc.DefaultRpcClient;
import com.bard.rpc.RpcClient;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/11/8
 */
public class ClientTest {

    public static void main(String[] args) {
        RpcClient client = new DefaultRpcClient("127.0.0.1", 10099);
        HelloWorldInface inface = client.refer(HelloWorldInface.class);
        String result = inface.helloWorld("RPC Test");

        System.out.println("返回结果:" + result);
    }
}
