package com.bard.server;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/11/8
 */
public class ServerTest {

    public void serverTest() {
        BardRpcServer server = new BardRpcServer();
        // 启动服务
        server.setPort(10099);
        try {
            server.startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 注册服务
        //server.registerService();

    }
}
