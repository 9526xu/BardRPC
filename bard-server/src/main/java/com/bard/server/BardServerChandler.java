package com.bard.server;

import com.bard.transport.BardRpcRequest;
import com.bard.transport.BardRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/11/2
 */
@Slf4j
public class BardServerChandler extends SimpleChannelInboundHandler<BardRpcRequest> {

    private Map<String, Object> instanceMap;

    public BardServerChandler(Map<String, Object> instanceMap) {
        this.instanceMap = instanceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BardRpcRequest msg) throws Exception {
        BardRpcResponse response = new BardRpcResponse();
        Object instance = instanceMap.get(msg.getIfaceName());
        response.setRequestId(msg.getRequestId());
        if (instance == null) {
            log.error("{}服务实例不存在", msg.getIfaceName());
            response.setError(new RuntimeException("服务实例不存在"));
            ctx.writeAndFlush(response);
        }
        Method method = instance.getClass().getMethod(msg.getMethodName(), msg.getParamTypes());

        Object result = method.invoke(instance, msg.getArgs());
        response.setObject(result);
        ctx.writeAndFlush(response);
    }
}
