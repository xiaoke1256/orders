package com.xiaoke1256.orders.core.service;

import com.alibaba.fastjson.JSON;
import com.xiaoke1256.orders.core.bo.PaymentTxn;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Transactional
    @Override
    public void onMessage(String paymentJson) {
        LOG.info("收到延迟消息：{}",paymentJson);
        PaymentTxn payment =JSON.parseObject(paymentJson, PaymentTxn.class);
        Long paymentId = payment.getPaymentId();
        PaymentTxn paymentEntity = entityManager.find(PaymentTxn.class, paymentId, LockModeType.PESSIMISTIC_WRITE);
        if (paymentEntity != null && PaymentTxn.PAY_STATUS_INIT.equals(paymentEntity.getPayStatus())){
            paymentEntity.setPayStatus(PaymentTxn.PAY_STATUS_EXPIRED);
            paymentEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            entityManager.merge(paymentEntity);
        }

    }
}
