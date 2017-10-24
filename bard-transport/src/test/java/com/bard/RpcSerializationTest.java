package com.bard;

import com.bard.serialization.RpcSerialization;
import com.bard.serialization.impl.KryoRpcSerialization;
import com.bard.transport.BardRpcRequest;
import org.junit.Test;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/23
 */
public class RpcSerializationTest {

    @Test
    public void kryoRpcSerializationTest() {
        RpcSerialization serialization = new KryoRpcSerialization();
        BardRpcRequest request = new BardRpcRequest("tasdas", "asdasdas", null, null, 54321L);
        byte[] bytes = serialization.serialize(request);

        BardRpcRequest result = serialization.deserialize(bytes, BardRpcRequest.class);
        System.out.println(request);
    }
}
