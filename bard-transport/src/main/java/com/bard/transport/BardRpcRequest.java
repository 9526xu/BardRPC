package com.bard.transport;

import lombok.Builder;
import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/23
 * <p>
 * RPC 发送消息类
 */

@Builder
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
    @Builder.Default
    private final long requestId = REQUEST_ID.incrementAndGet();


}
