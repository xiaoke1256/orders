package com.xiaoke1256.thirdpay.domain.model.repository.impl;

import com.xiaoke1256.thirdpay.domain.model.aggregate.ThirdPayOrder;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderNo;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderStatus;
import com.xiaoke1256.thirdpay.domain.model.repository.ThirdPayOrderRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ThirdPayOrderRepository的内存实现
 * 用于开发和测试环境
 */
@Repository
public class ThirdPayOrderRepositoryImpl implements ThirdPayOrderRepository {
    
    // 内存存储，实际应用中应该使用数据库
    private final Map<String, ThirdPayOrder> orderStore = new ConcurrentHashMap<>();
    private final Random random = new Random();
    
    @Override
    public ThirdPayOrder save(ThirdPayOrder order) {
        if (order == null || order.getOrderNo() == null) {
            throw new IllegalArgumentException("Order or orderNo cannot be null");
        }
        orderStore.put(order.getOrderNo().getValue(), order);
        return order;
    }
    
    @Override
    public Optional<ThirdPayOrder> findByOrderNo(OrderNo orderNo) {
        if (orderNo == null || orderNo.getValue() == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(orderStore.get(orderNo.getValue()));
    }
    
    @Override
    public Optional<ThirdPayOrder> findByMerchantOrderNo(String merchantNo, String merchantOrderNo) {
        if (merchantNo == null || merchantOrderNo == null) {
            return Optional.empty();
        }
        return orderStore.values().stream()
                .filter(order -> merchantNo.equals(order.getMerchantNo()) && 
                                 merchantOrderNo.equals(order.getMerchantOrderNo()))
                .findFirst();
    }
    
    @Override
    public List<ThirdPayOrder> findByStatusAndTimeRange(OrderStatus status, LocalDateTime startTime, LocalDateTime endTime) {
        if (status == null) {
            return Collections.emptyList();
        }
        return orderStore.values().stream()
                .filter(order -> status.equals(order.getOrderStatus()) &&
                                 (startTime == null || !order.getInsertTime().isBefore(startTime)) &&
                                 (endTime == null || !order.getInsertTime().isAfter(endTime)))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ThirdPayOrder> findExpiredOrders(LocalDateTime currentTime, int timeoutMinutes) {
        if (currentTime == null) {
            return Collections.emptyList();
        }
        LocalDateTime thresholdTime = currentTime.minusMinutes(timeoutMinutes);
        return orderStore.values().stream()
                .filter(order -> OrderStatus.STATUS_ACCEPT.equals(order.getOrderStatus()) &&
                                 order.getInsertTime().isBefore(thresholdTime))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean updateOrderStatus(OrderNo orderNo, OrderStatus status, LocalDateTime finishTime) {
        ThirdPayOrder order = orderStore.get(orderNo.getValue());
        if (order != null) {
            // 在实际应用中，这里应该更新订单状态
            // 由于这是内存实现，我们可以创建一个新的订单对象
            // 注意：这只是一个简单的模拟实现
            return true;
        }
        return false;
    }
    
    @Override
    public boolean deleteByOrderNo(OrderNo orderNo) {
        if (orderNo != null && orderNo.getValue() != null) {
            return orderStore.remove(orderNo.getValue()) != null;
        }
        return false;
    }
    
    @Override
    public OrderNo generateOrderNo() {
        return OrderNo.generate();
    }
}