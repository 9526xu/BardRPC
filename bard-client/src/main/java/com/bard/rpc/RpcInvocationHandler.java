package com.bard.rpc;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/20
 */
@Slf4j
public class RpcInvocationHandler implements InvocationHandler {


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

        log.debug("method name is " + methodName + " parameterTypes is " + parameterTypes);

        return "hello World";
    }
}
