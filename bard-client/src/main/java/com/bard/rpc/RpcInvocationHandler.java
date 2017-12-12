package com.bard.rpc;

import com.bard.codec.BardRpcClientCodec;
import com.bard.rpc.client.BardClientConnector;
import com.bard.rpc.client.BardClientConnectorFactory;
import com.bard.transport.BardRpcRequest;
import com.bard.transport.BardRpcResponse;
import com.google.common.reflect.AbstractInvocationHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/20
 * 继承 guava 的AbstractInvocationHandler
 * 处理 Object 自带 toString hashcode equals
 * 主要发现 debug 的时候调用代理方法 导致报错
 */
@Slf4j
public class RpcInvocationHandler extends AbstractInvocationHandler {

    private RpcConnectConfig config;


    public RpcInvocationHandler(RpcConnectConfig config) {
        this.config = config;
    }

    public RpcInvocationHandler() {
    }


    @Override
    protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
        return rpcInvoke(method, args);
    }

    /**
     * 实现 Rpc 调用的相关
     *
     * @param method
     * @param args
     * @return
     */
    private Object rpcInvoke(Method method, Object[] args) throws Exception {
        //获取方法名 参数等信息
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        String ifaceName = method.getDeclaringClass().getName();
        BardRpcRequest request = new BardRpcRequest(ifaceName, methodName, args, parameterTypes, config.getConnectTimeOut());
        // 获取 Connector 类
        BardClientConnector clientConnector = BardClientConnectorFactory.newNettyClientInstance(config);
        CallBackService callBackService = clientConnector.sendRequest(request);

        BardRpcResponse response = (BardRpcResponse) callBackService.getResult();
        if (response.getObject() != null) {
            return response.getObject();
        } else {
            throw new RuntimeException("RPC 调用异常");
        }
    }

    /**
     * 发送请求
     *
     * @param request
     * @param config
     * @return
     */
    private BardRpcResponse sendRequest(BardRpcRequest request, RpcConnectConfig config) throws Exception {
        final CountDownLatch doneSignal = new CountDownLatch(1);
        final BardRpcResponse response = new BardRpcResponse();
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(config.getHost(), config.getPort()).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast("clientCodec", new BardRpcClientCodec());
                pipeline.addLast("clientBusiness", new SimpleChannelInboundHandler<BardRpcResponse>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, BardRpcResponse msg) throws Exception {
                        response.setObject(msg.getObject());
                        response.setError(msg.getError());
                        response.setRequestId(msg.getRequestId());
                        response.setCode(msg.getCode());
                        doneSignal.countDown();
                    }
                });
            }
        }).option(ChannelOption.SO_KEEPALIVE, true);

        try {

            ChannelFuture channelFuture = bootstrap.connect().sync();
            Channel channel = channelFuture.channel();
            ChannelFuture sendFeature = channel.writeAndFlush(request);

            sendFeature.addListener(future -> {
                if (future.isSuccess()) {
                    log.debug("发送成功");
                } else {
                    log.error("发送失败");
                }
            });
            doneSignal.await();

            channel.close().sync();
            channel.closeFuture().sync();


            return response;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
        }
        return response;

    }
}
