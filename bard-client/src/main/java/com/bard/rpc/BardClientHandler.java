package com.bard.rpc;

import com.bard.transport.BardRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/24
 */
public class BardClientHandler extends SimpleChannelInboundHandler<BardRpcResponse> {

    private Map<Long, CallBackService> callBackMap;

    public BardClientHandler(Map<Long, CallBackService> callBackMap) {
        this.callBackMap = callBackMap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BardRpcResponse bardRpcResponse) {
        Long requestId = bardRpcResponse.getRequestId();
        // 取出处理方法
        CallBackService callBackService = callBackMap.get(requestId);
        //todo 如果 callBack 不存在
        callBackService.setDone(bardRpcResponse);
    }
}
