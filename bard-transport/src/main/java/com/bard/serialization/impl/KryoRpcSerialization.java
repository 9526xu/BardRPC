package com.bard.serialization.impl;

import com.bard.serialization.RpcSerialization;
import com.bard.transport.BardRpcRequest;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;

import java.io.ByteArrayOutputStream;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/23
 * <p>
 * kryo 序列化与反序列化实现
 */
public class KryoRpcSerialization implements RpcSerialization {
    /**
     * 根据官方例子构造
     * 线程安全
     */
    private KryoPool pool = new KryoPool.Builder(() -> {
        Kryo kryo = new Kryo();
        kryo.register(BardRpcRequest.class);
        // configure kryo instance, customize settings
        return kryo;
    }).softReferences().build();

    @Override
    public <T> byte[] serialize(T object) {

        return pool.run(kryo -> {
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            Output output = new Output(arrayOutputStream);
            kryo.writeObject(output, object);

            output.close();

            return output.getBuffer();
        });

    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> tClass) {
        return pool.run(kryo -> {
            Input input = new Input(bytes);
            T someObject = kryo.readObject(input, tClass);
            input.close();

            return someObject;
        });
    }
}
