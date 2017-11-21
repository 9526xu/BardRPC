package com.bard.server;

import com.bard.codec.BardRpcServerCodec;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/11/2
 * <p>
 * RPC server 类
 */
@Slf4j
public class BardRpcServer {

    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * key：服务名字
     * value:服务的对象
     */
    private Map<String, Object> instanceMap = Maps.newConcurrentMap();

    /**
     * 注册服务实例
     *
     * @param serviceName 服务名
     * @param instance    实例
     */
    public void registerService(String serviceName, Object instance) {
        Preconditions.checkArgument(StringUtils.isNotBlank(serviceName), "服务名不能为空");
        if (instanceMap.containsKey(serviceName)) {
            throw new RuntimeException("服务名已存在,不允许重复");
        }
        instanceMap.put(serviceName, instance);
    }

    public void registerService(Object instance) {
        //fixme 暂且设计类智能实现一个接口
        List<Class<?>> list = ClassUtils.getAllInterfaces(instance.getClass());
        if (list == null && list.size() == 0) {
            throw new RuntimeException("这个类未实现接口");
        } else if (list.size() > 1) {
            throw new RuntimeException("这个类目前只能实现一个接口");
        }


        String serviceName = list.get(0).getSimpleName();
        registerService(serviceName, instance);
    }

    /**
     * 获取服务实例
     *
     * @param serviceName
     * @return
     */
    public Object getInstance(String serviceName) {
        Preconditions.checkArgument(StringUtils.isBlank(serviceName), "服务名不能为空");
        Object obj = instanceMap.get(serviceName);
        if (obj == null) {
            throw new RuntimeException("服务实例不存在");
        }

        return obj;

    }

    /**
     * 启动服务端程序
     */
    public void startServer() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();


        try {

            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new BardServerChandler(instanceMap));
                            ch.pipeline().addLast(new BardRpcServerCodec());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);


            ChannelFuture future = bootstrap.bind().sync();
            log.debug("服务启动成功,port:{}", port);

            future.channel().closeFuture().sync();


        } finally {
            group.shutdownGracefully().sync();
        }

    }


}
