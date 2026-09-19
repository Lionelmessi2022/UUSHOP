package com.test.service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RocketMQMessageListener(topic = "myTopic", consumerGroup = "message")
public class MessageService implements RocketMQListener<String> {

    @Autowired
    private WebSocket webSocket;


    @Override
    public void onMessage(String message) {

        System.out.println("收到了消息: " + message);
        this.webSocket.sendMessage(message);
    }

}

