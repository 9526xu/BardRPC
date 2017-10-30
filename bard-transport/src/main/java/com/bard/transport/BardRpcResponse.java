package com.bard.transport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/10/24
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BardRpcResponse {
    /**
     * 请求 id
     */
    private long requestId;
    /**
     * 状态码
     */
    private String code;
    /**
     * 返回对象值
     */
    private Object object;
    /**
     * 异常信息
     */
    private Throwable error;


}
