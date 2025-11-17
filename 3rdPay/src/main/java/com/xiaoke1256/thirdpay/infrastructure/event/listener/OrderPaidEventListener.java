package com.xiaoke1256.thirdpay.infrastructure.event.listener;

import com.xiaoke1256.thirdpay.domain.event.OrderPaidEvent;
import com.xiaoke1256.thirdpay.infrastructure.event.DomainEventAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 订单支付成功事件监听器
 */
@Component
public class OrderPaidEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderPaidEventListener.class);

    @EventListener
    public void handleOrderPaidEvent(DomainEventAdapter eventAdapter) {
        if (eventAdapter.getDomainEvent() instanceof OrderPaidEvent) {
            OrderPaidEvent event = (OrderPaidEvent) eventAdapter.getDomainEvent();
            logger.info("Order paid successfully: {}, eventId: {}", 
                    event.getOrderNo().getValue(), event.getEventId());
            
            // 这里可以实现具体的业务逻辑，比如：
            // 1. 发送通知给商户
            // 2. 更新相关业务系统的状态
            // 3. 记录日志
            
            // 模拟发送通知给商户
            sendMerchantNotification(event);
        }
    }

    private void sendMerchantNotification(OrderPaidEvent event) {
        // 实际项目中可能需要调用通知服务
        logger.info("Sending payment notification to merchant: {}, orderNo: {}",
                event.getOrder().getMerchantNo(), event.getOrderNo().getValue());
    }
}