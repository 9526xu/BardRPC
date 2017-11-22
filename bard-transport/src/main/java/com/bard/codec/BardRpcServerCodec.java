package com.bard.codec;

import com.bard.serialization.RpcSerialization;
import com.bard.serialization.SerializationFactory;
import com.bard.serialization.impl.KryoRpcSerialization;
import com.bard.transport.BardRpcRequest;
import com.bard.transport.BardRpcResponse;
import com.bard.utils.UnpackUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/11/2
 * 服务端编解码
 */
public class BardRpcServerCodec extends ByteToMessageCodec<BardRpcResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, BardRpcResponse msg, ByteBuf out) throws Exception {
        RpcSerialization serialization = SerializationFactory.cerateKryoSerializatio();
        byte[] bytes = serialization.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        byte[] bytes = UnpackUtils.unpackByteBuf(in);
        if (bytes == null) {
            return;
        }
        RpcSerialization serialization = new KryoRpcSerialization();
        BardRpcRequest request = serialization.deserialize(bytes, BardRpcRequest.class);
        out.add(request);
    }
}
