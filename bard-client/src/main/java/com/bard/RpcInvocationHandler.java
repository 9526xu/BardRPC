package com.bard;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/20
 */
@Slf4j
public class RpcInvocationHandler implements InvocationHandler {

    private Object target;

    public RpcInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //实现 发送等逻辑
        log.debug("hello Invocation" + method.getName());
        return method.invoke(target, args);
    }
}
