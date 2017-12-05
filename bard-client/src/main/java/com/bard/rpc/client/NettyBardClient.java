package com.bard.rpc.client;

import com.alibaba.fastjson.JSON;
import com.bard.codec.BardRpcClientCodec;
import com.bard.rpc.RpcConnectConfig;
import com.bard.transport.BardRpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author andyXu xu9529@gmail.com
 * @date 2017/12/5
 * client netty 实现
 */
@Slf4j
public class NettyBardClient implements BardClient {

    private RpcConnectConfig config;

    private Bootstrap bootstrap;

    private EventLoopGroup eventLoopGroup;

    private Channel channel;


    public NettyBardClient(RpcConnectConfig config) {
        this.config = config;
    }

    public NettyBardClient(String host, int port) {
        this(new RpcConnectConfig(host, port));
    }

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
    public void sendRequest(BardRpcRequest request) {
        // channel 为空或未就绪
        if (channel == null || !channel.isActive()) {
            throw new RuntimeException("channel 尚未创建或者 channel 连接未就绪");
        }
        ChannelFuture sendFeature = channel.writeAndFlush(request);

        sendFeature.addListener(future -> {
            if (future.isSuccess()) {
                log.debug("请求数据:{},发送成功", JSON.toJSONString(request));
            } else {
                log.error("请求数据:{},发送失败", JSON.toJSONString(request));
            }
        });

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
