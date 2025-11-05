package com.xiaoke1256.orders.pay.service.mq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/** 测试消费者 */
@Service
@RocketMQMessageListener(topic = "test1", consumerGroup = "consumerGroup")
public class TestMsgConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("receive: " + s);
    }
}
