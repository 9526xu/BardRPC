package com.bard.utils;

import io.netty.buffer.ByteBuf;

/**
 * @author andyXu xu9529@gmail.com
 * @date 2017/11/21
 * netty 通用拆包
 * <p>
 * 发送的协议 消息大小(int)+消息
 */
public class UnpackUtils {

    public final static int HEAD_LENGTH = 4;

    public static byte[] unpackByteBuf(ByteBuf in) {
        // 消息头为消息的长度 int 至少为4
        if (in.readableBytes() < HEAD_LENGTH) {
            return null;
        }
        // 标记当前reader index
        in.markReaderIndex();

        int headLength = in.readInt();
        // 如果此时 可以读取 bytes 小于 传递过来的字节数
        if (in.readableBytes() < headLength) {
            // 将 可读字节数重置到标记的位置 保证每次读取到头字节
            in.resetReaderIndex();
            return null;
        }
        byte[] bytes = new byte[headLength];
        in.readBytes(bytes);
        return bytes;
    }

}
