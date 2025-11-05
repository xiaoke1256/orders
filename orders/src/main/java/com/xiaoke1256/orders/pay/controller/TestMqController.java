package com.xiaoke1256.orders.pay.controller;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.RespMsg;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testMq")
public class TestMqController {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 测试发送消息
     * @return
     */
    @RequestMapping(value="/testSendMsg",method= {RequestMethod.POST})
    public RespMsg payNotice() {
        Message<String> strMessage = MessageBuilder.withPayload("test" ).setHeader(RocketMQHeaders.KEYS, "testId").build();
        SendResult result = rocketMQTemplate.syncSend("test1", "test");
        return new RespMsg(RespCode.SUCCESS);
    }
}
