package com.bard.serialization;

import com.bard.serialization.impl.KryoRpcSerialization;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/11/2
 */
public class SerializationFactory {
    /**
     * Kryo序列化实现
     *
     * @return
     */
    public static RpcSerialization cerateKryoSerializatio() {

        return new KryoRpcSerialization();
    }

}

