package com.bard.serialization.impl;

import com.bard.serialization.RpcSerialization;
import com.google.common.base.Preconditions;
import org.apache.thrift.TSerializer;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/23
 * Thrift 序列化与反序列化实现
 * 查询资料 Thrift 序列化与反序列化 基于 IDL模板与现在写的暂时不符 故先放弃
 */
public class ThriftRpcSerialization implements RpcSerialization {

    @Override
    public <T> byte[] serialize(T object) {
        Preconditions.checkNotNull(object);
        TSerializer serializer = new TSerializer();
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> tClass) {
        return null;
    }
}
