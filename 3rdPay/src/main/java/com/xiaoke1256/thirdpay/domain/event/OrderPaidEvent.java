package com.xiaoke1256.thirdpay.domain.event;

import com.xiaoke1256.thirdpay.domain.model.aggregate.ThirdPayOrder;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderNo;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 订单支付成功事件
 */
public class OrderPaidEvent implements DomainEvent {
    private String eventId;
    private OrderNo orderNo;
    private ThirdPayOrder order;
    private LocalDateTime timestamp;

    public OrderPaidEvent(OrderNo orderNo, ThirdPayOrder order) {
        this.eventId = UUID.randomUUID().toString();
        this.orderNo = orderNo;
        this.order = order;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    public OrderNo getOrderNo() {
        return orderNo;
    }

    public ThirdPayOrder getOrder() {
        return order;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}