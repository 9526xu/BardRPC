package com.bard.transport;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/23
 * <p>
 * RPC 发送消息类
 */
@NoArgsConstructor
@Data
public class BardRpcRequest {

    private static AtomicLong REQUEST_ID = new AtomicLong(0L);

    /**
     * 接口名
     */
    private String ifaceName;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 方法参数
     */
    private Object[] args;
    /**
     * 方法参数类型
     */
    private Class<?>[] paramTypes;
    /**
     * 超时时间
     */
    private Long timeOut;
    /**
     * 请求 id
     */
    private final long requestId = REQUEST_ID.incrementAndGet();

    public BardRpcRequest(String ifaceName, String methodName, Object[] args, Class<?>[] paramTypes, Long timeOut) {
        this.ifaceName = ifaceName;
        this.methodName = methodName;
        this.args = args;
        this.paramTypes = paramTypes;
        this.timeOut = timeOut;
    }
}
