package com.bard.serialization;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/23
 * 序列化工具
 */
public interface RpcSerialization {
    /**
     * 对象序列化成字节数组
     *
     * @param object
     * @param <T>
     * @return
     */
    <T> byte[] serialize(T object);

    /**
     * 字节数组反序列化成对象
     *
     * @param bytes
     * @param tClass
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] bytes, Class<T> tClass);

}
