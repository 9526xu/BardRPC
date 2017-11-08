package com.bard.Test;

import com.bard.server.BardRpcServer;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/11/8
 */
public class ServerTest {

    public static void main(String[] args) {
        BardRpcServer server = new BardRpcServer();

        server.setPort(10099);
        server.registerService(new HelloWorldInfaceImpl());
        try {
            server.startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
