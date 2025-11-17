package com.xiaoke1256.thirdpay.infrastructure.event.listener;

import com.xiaoke1256.thirdpay.domain.event.OrderFailedEvent;
import com.xiaoke1256.thirdpay.infrastructure.event.DomainEventAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 订单支付失败事件监听器
 */
@Component
public class OrderFailedEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderFailedEventListener.class);

    @EventListener
    public void handleOrderFailedEvent(DomainEventAdapter eventAdapter) {
        if (eventAdapter.getDomainEvent() instanceof OrderFailedEvent) {
            OrderFailedEvent event = (OrderFailedEvent) eventAdapter.getDomainEvent();
            logger.info("Order payment failed: {}, reason: {}, eventId: {}",
                    event.getOrderNo().getValue(), event.getFailureReason(), event.getEventId());
            
            // 这里可以实现具体的业务逻辑，比如：
            // 1. 发送失败通知给商户
            // 2. 记录错误日志
            // 3. 触发补偿机制
            
            // 模拟发送失败通知给商户
            sendFailureNotification(event);
        }
    }

    private void sendFailureNotification(OrderFailedEvent event) {
        // 实际项目中可能需要调用通知服务
        logger.info("Sending payment failure notification to merchant: {}, orderNo: {}, reason: {}",
                event.getOrder().getMerchantNo(), 
                event.getOrderNo().getValue(),
                event.getFailureReason());
    }
}