package com.bard.rpc;

import com.bard.serialization.RpcSerialization;
import com.bard.serialization.impl.KryoRpcSerialization;
import com.bard.transport.BardRpcRequest;
import com.bard.transport.BardRpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/20
 */
@Slf4j
public class RpcInvocationHandler implements InvocationHandler {

    private RpcConnectConfig config;


    public RpcInvocationHandler(RpcConnectConfig config) {
        this.config = config;
    }

    public RpcInvocationHandler() {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 如果是接口
        return rpcInvoke(method, args);


    }

    /**
     * 实现 Rpc 调用的相关
     *
     * @param method
     * @param args
     * @return
     */
    private Object rpcInvoke(Method method, Object[] args) {
        //获取方法名 参数等信息
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        String ifaceName = method.getDeclaringClass().getName();

        BardRpcRequest request = new BardRpcRequest(ifaceName, methodName, args, parameterTypes, config.getConnectTimeOut());
        BardRpcResponse response = sendRequest(request, config);

        return "hello World";
    }

    /**
     * 发送请求
     *
     * @param request
     * @param config
     * @return
     */
    private BardRpcResponse sendRequest(BardRpcRequest request, RpcConnectConfig config) {
        // 序列化
        RpcSerialization serialization = new KryoRpcSerialization();
        byte[] bytes = serialization.serialize(request);

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(config.getHost(), config.getPort()).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new BardClientHandler());
            }
        }).option(ChannelOption.SO_KEEPALIVE, true);

        try {
            ChannelFuture channelFuture = bootstrap.bind().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }
}
