package com.bard.rpc;

import com.alibaba.fastjson.JSON;
import com.bard.transport.BardRpcRequest;
import com.bard.transport.BardRpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author andyXu xu9529@gmail.com
 * @date 2017/12/11
 * 处理回调的服务
 * 一个 client 的请求对用一个这个对象
 */
@Slf4j
public class CallBackService {

    private BardRpcResponse response;

    private BardRpcRequest request;

    private boolean isDone = false;

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition doneCondition = lock.newCondition();

    public CallBackService(BardRpcRequest request) {
        this.request = request;
    }

    public CallBackService() {
    }

    public Object getResult() {
        lock.lock();
        try {
            while (!isDone) {
                doneCondition.await();
            }
        } catch (Exception e) {
            log.error("CallBackService 获取返回对象异常,请求信息{}", JSON.toJSONString(request), e);
        } finally {
            lock.unlock();
        }
        return response;
    }


    public void setDone(BardRpcResponse response) {
        lock.lock();
        try {
            this.response = response;
            this.isDone = true;
            doneCondition.signalAll();
        } finally {
            lock.unlock();
        }

    }
}
