package com.bard.rpc;

import com.alibaba.fastjson.JSON;
import com.bard.transport.BardRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @author andyXu xu9529@gmail.com
 * @date 2017/11/22
 */
@Slf4j
public class BardClientChandler extends SimpleChannelInboundHandler<BardRpcResponse> {

    private BardRpcResponse response;

    private CountDownLatch doneSignal;

    public BardClientChandler(CountDownLatch doneSignal) {
        this.doneSignal = doneSignal;
    }


    public BardRpcResponse getResponse() {
        return response;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BardRpcResponse msg) throws Exception {
        log.debug("server reponse is{}", JSON.toJSONString(msg));
        this.response = msg;
        doneSignal.countDown();
    }
}
