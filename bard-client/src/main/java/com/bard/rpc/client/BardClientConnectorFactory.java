package com.bard.rpc.client;

import com.bard.rpc.RpcConnectConfig;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author andyXu xu9529@gmail.com
 * @date 2017/12/11
 */
public class BardClientConnectorFactory {

    private final Map<String, BardClientConnector> clientMap = Maps.newConcurrentMap();

    public BardClientConnector getNettyClientInstance(RpcConnectConfig config) {
        check(config);
        // 先查看 map 中是否存在已经创建好的 client
        BardClientConnector client = clientMap.getOrDefault(config.getAddress(), new NettyBardClientConnector(config));
        return client;
    }

    private void check(RpcConnectConfig config) {
        Preconditions.checkNotNull(config);
        Preconditions.checkArgument(StringUtils.isBlank(config.getHost()));
        Preconditions.checkArgument(config.getPort() == null);
    }
}
