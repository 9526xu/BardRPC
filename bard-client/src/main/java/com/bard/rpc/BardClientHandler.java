package com.bard.rpc;

import com.bard.transport.BardRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/24
 */
public class BardClientHandler extends SimpleChannelInboundHandler<BardRpcResponse> {

    private BardRpcResponse response;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BardRpcResponse bardRpcResponse) throws Exception {
        this.response = bardRpcResponse;
    }
}
