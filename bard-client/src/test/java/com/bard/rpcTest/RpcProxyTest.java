package com.bard.rpcTest;

import com.bard.rpc.RpcInvocationHandler;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/20
 */
public class RpcProxyTest {
    @Test
    public void rpcProxyTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // 获取代理类
       /* Class<?> proxyClass = Proxy.getProxyClass(RpcProxyTest.class.getClassLoader(), HelloWorldInface.class);
        Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
        InvocationHandler invocationHandler = new RpcInvocationHandler(new HelloWorldInface() {
            @Override
            public void helloWorld(String text) {
                System.out.println("hello world" + text);
            }
        });
        HelloWorldInface inface = (HelloWorldInface) constructor.newInstance(invocationHandler);
        inface.helloWorld("1231231321");*/

        HelloWorldInface inface = (HelloWorldInface) Proxy.newProxyInstance(RpcProxyTest.class.getClassLoader(), new Class[]{HelloWorldInface.class}, new RpcInvocationHandler());
        String result = inface.helloWorld("adasdasdsaas");


    }
}
