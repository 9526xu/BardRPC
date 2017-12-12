package com.bard.rpc.client;

import com.bard.rpc.CallBackService;
import com.bard.transport.BardRpcRequest;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author andyXu xu9529@gmail.com
 * @date 2017/12/5
 * 客户端相关接口
 */
public interface BardClientConnector extends Closeable {


    void connect();

    CallBackService sendRequest(BardRpcRequest request);

    @Override
    void close() throws IOException;
}
