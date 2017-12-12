package com.bard.rpc.client;

import com.alibaba.fastjson.JSON;
import com.bard.codec.BardRpcClientCodec;
import com.bard.rpc.BardClientHandler;
import com.bard.rpc.CallBackService;
import com.bard.rpc.RpcConnectConfig;
import com.bard.transport.BardRpcRequest;
import com.google.common.collect.Maps;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * @author andyXu xu9529@gmail.com
 * @date 2017/12/5
 * client netty 实现
 */
@Slf4j
public class NettyBardClientConnector implements BardClientConnector {

    private RpcConnectConfig config;

    private Bootstrap bootstrap;

    private EventLoopGroup eventLoopGroup;

    private Channel channel;
    /**
     * requestId 与返回处理函数
     */
    private Map<Long, CallBackService> callBackMap = Maps.newConcurrentMap();


    public NettyBardClientConnector(RpcConnectConfig config) {
        this.config = config;
    }

    public NettyBardClientConnector(String host, int port) {
        this(new RpcConnectConfig(host, port));
    }

    // todo 并发问题
    @Override
    public void connect() {
        log.info("start connect");
        try {
            eventLoopGroup = new NioEventLoopGroup();
            bootstrap = new Bootstrap();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(config.getHost(), config.getPort()).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast("clientCodec", new BardRpcClientCodec());
                    pipeline.addLast("busProcess", new BardClientHandler(callBackMap));
                }
            }).option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = bootstrap.connect().sync();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            log.error("连接发生错误", e);
            throw new RuntimeException("客户端连接异常");
        }

    }

    @Override
    public CallBackService sendRequest(BardRpcRequest request) {
        // channel 为空或未就绪
        if (channel == null || !channel.isActive()) {
            connect();
        }
        ChannelFuture sendFeature = channel.writeAndFlush(request);
        CallBackService callBackService = new CallBackService(request);
        callBackMap.put(request.getRequestId(), callBackService);
        sendFeature.addListener(future -> {
            if (future.isSuccess()) {
                log.debug("请求数据:{},发送成功", JSON.toJSONString(request));
            } else {
                log.error("请求数据:{},发送失败", JSON.toJSONString(request));
            }
        });

        return callBackService;

    }


    @Override
    public void close() throws IOException {

        if (eventLoopGroup == null || eventLoopGroup.isShutdown()) {
            return;
        }
        try {
            eventLoopGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
