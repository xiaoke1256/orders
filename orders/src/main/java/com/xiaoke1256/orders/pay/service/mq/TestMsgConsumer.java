package com.xiaoke1256.orders.pay.service.mq;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/** 测试消费者 */
@Service
@RocketMQMessageListener(topic = "test1", consumerGroup = "testConsumerGroup",consumeMode = ConsumeMode.ORDERLY, enableMsgTrace = true)
public class TestMsgConsumer implements RocketMQListener<String> {
    @Override
    public synchronized void  onMessage(String s) {
        System.out.println("receive: " + s);
    }
}
