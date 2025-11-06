package com.xiaoke1256.orders.core.service;

import com.alibaba.fastjson.JSON;
import com.xiaoke1256.orders.core.bo.PaymentTxn;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;

/**
 * 清理过期订单的消费者
 */
@Service
@RocketMQMessageListener(topic = "clear_expired_order", consumerGroup = "consumerGroup")
public class ClearExpiredOrderConsumer implements RocketMQListener<String> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);

    @PersistenceContext(unitName="default")
    private EntityManager entityManager ;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Transactional
    @Override
    public void onMessage(String paymentJson) {
        LOG.info("收到延迟消息：{}",paymentJson);
        PaymentTxn payment =JSON.parseObject(paymentJson, PaymentTxn.class);
        Long paymentId = payment.getPaymentId();
        PaymentTxn paymentEntity = entityManager.find(PaymentTxn.class, paymentId, LockModeType.PESSIMISTIC_WRITE);
        if (paymentEntity != null && PaymentTxn.PAY_STATUS_INIT.equals(paymentEntity.getPayStatus())){
            //如果延迟时间未到，重新发送消息
            if(paymentEntity.getInsertTime().getTime() + 5 * 60 * 1000 > System.currentTimeMillis()){
                LOG.info("重新发送延迟消息：{}",paymentJson);
                rocketMQTemplate.syncSend("clear_expired_order", MessageBuilder.withPayload(JSON.toJSONString(payment) )
                        .setHeader(RocketMQHeaders.MESSAGE_ID, payment.getPaymentId()).build(), 2000, 9);
                return;
            }
            paymentEntity.setPayStatus(PaymentTxn.PAY_STATUS_EXPIRED);
            paymentEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            entityManager.merge(paymentEntity);
            LOG.info("订单支付超时：{}",paymentEntity.getPayOrderNo());
        }

    }
}
