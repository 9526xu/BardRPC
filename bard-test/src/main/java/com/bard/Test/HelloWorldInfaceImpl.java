package com.bard.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author andyXu xu9529@gmail.com
 * @Date 2017/11/8
 */
@Slf4j
public class HelloWorldInfaceImpl implements HelloWorldInface {
    @Override
    public String helloWorld(String text) {
        log.info("hello world 真正实现");
        return "RPC SERVER  hello world";
    }
}
