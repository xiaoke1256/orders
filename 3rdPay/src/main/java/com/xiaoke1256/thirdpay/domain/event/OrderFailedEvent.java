package com.xiaoke1256.thirdpay.domain.event;

import com.xiaoke1256.thirdpay.domain.model.aggregate.ThirdPayOrder;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderNo;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 订单支付失败事件
 */
public class OrderFailedEvent implements DomainEvent {
    private String eventId;
    private OrderNo orderNo;
    private ThirdPayOrder order;
    private String failureReason;
    private LocalDateTime timestamp;

    public OrderFailedEvent(OrderNo orderNo, ThirdPayOrder order, String failureReason) {
        this.eventId = UUID.randomUUID().toString();
        this.orderNo = orderNo;
        this.order = order;
        this.failureReason = failureReason;
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

    public String getFailureReason() {
        return failureReason;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}