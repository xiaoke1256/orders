package com.xiaoke1256.orders.pay.controller;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.RespMsg;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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
        SendStatus sendStatus = result.getSendStatus();
        if(!SendStatus.SEND_OK.equals(sendStatus)){
            //TODO 做回滚处理
        }
        return new RespMsg(RespCode.SUCCESS);
    }

    @RequestMapping(value="/testSendMsgBatch",method= {RequestMethod.POST})
    public RespMsg payNoticeBatch() {
        System.out.println("测试发送批量消息");
        Date now = new Date();
        String prefix= String.valueOf(now.getTime());

        for(int i=0;i<100;i++) {
            String key = prefix+"-"+i;
            System.out.println("key:"+key);
            Message<String> strMessage = MessageBuilder.withPayload(key).setHeader(RocketMQHeaders.KEYS, key).build();
            //rocketMQTemplate.syncSend("test1", strMessage, 3000, 3);
            rocketMQTemplate.syncSendOrderly("test1", strMessage,"queue1", 3000, 3);
        }
        System.out.println("测试发送批量消息结束");

        return new RespMsg(RespCode.SUCCESS);
    }
}
