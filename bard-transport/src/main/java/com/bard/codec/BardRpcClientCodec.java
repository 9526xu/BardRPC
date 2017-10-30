package com.bard.codec;

import com.bard.serialization.RpcSerialization;
import com.bard.serialization.impl.KryoRpcSerialization;
import com.bard.transport.BardRpcRequest;
import com.bard.transport.BardRpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/30
 * client 消息解码与编码
 */
public class BardRpcClientCodec extends ByteToMessageCodec<BardRpcRequest> {

    private BardRpcResponse response;

    public BardRpcClientCodec(BardRpcResponse response) {
        this.response = response;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, BardRpcRequest msg, ByteBuf out) throws Exception {
        RpcSerialization serialization = new KryoRpcSerialization();
        byte[] bytes = serialization.serialize(msg);
        out.writeBytes(bytes);

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readableBytes();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        RpcSerialization serialization = new KryoRpcSerialization();
        response = serialization.deserialize(bytes, BardRpcResponse.class);
        out.add(response);

    }
}
