package com.bard.rpc.client;

import com.bard.rpc.RpcConnectConfig;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author andyXu xu9529@gmail.com
 * @date 2017/12/11
 */
public class BardClientConnectorFactory {

    private final static Map<String, BardClientConnector> clientMap = Maps.newConcurrentMap();

    public static BardClientConnector newNettyClientInstance(RpcConnectConfig config) {
        check(config);
        // 先查看 map 中是否存在已经创建好的 client
        BardClientConnector client = clientMap.getOrDefault(config.getAddress(), new NettyBardClientConnector(config));
        clientMap.putIfAbsent(config.getAddress(), client);
        return client;
    }

    public static void closeNettyClient(RpcConnectConfig config) throws IOException {
        check(config);
        BardClientConnector client = clientMap.get(config.getAddress());
        if (client != null) {
            client.close();
        }

    }

    private static void check(RpcConnectConfig config) {
        Preconditions.checkNotNull(config);
        Preconditions.checkArgument(StringUtils.isNotBlank(config.getHost()));
        Preconditions.checkArgument(config.getPort() != null);
    }
}
