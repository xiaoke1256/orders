package com.xiaoke1256.thirdpay.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xiaoke1256.thirdpay.domain.model.aggregate.ThirdPayOrder;
import com.xiaoke1256.thirdpay.domain.model.repository.ThirdPayOrderRepository;
import com.xiaoke1256.thirdpay.domain.model.valueobject.AccountNo;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderNo;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderStatus;
//import com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder as
import com.xiaoke1256.thirdpay.payplatform.mapper.ThirdPayOrderMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 第三方支付订单仓储实现
 */
@Repository
public class ThirdPayOrderRepositoryImpl implements ThirdPayOrderRepository {

    @Autowired
    private ThirdPayOrderMapper orderMapper;

    @Override
    public ThirdPayOrder save(ThirdPayOrder order) {
        com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder bo = convertToBo(order);
        if (order.getOrderId() == null) {
            orderMapper.insert(bo);
        } else {
            orderMapper.updateById(bo);
        }
        order.setOrderId(bo.getOrderId());
        return order;
    }

    @Override
    public Optional<ThirdPayOrder> findByOrderNo(OrderNo orderNo) {
        LambdaQueryWrapper< com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq( com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder::getOrderNo, orderNo.getValue());
        com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder bo = orderMapper.selectOne(wrapper);
        return bo != null ? Optional.of(convertToAggregate(bo)) : Optional.empty();
    }

    @Override
    public Optional<ThirdPayOrder> findByMerchantOrderNo(String merchantNo, String merchantOrderNo) {
        LambdaQueryWrapper<com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder::getMerchantNo, merchantNo)
               .eq(com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder::getMerchantOrderNo, merchantOrderNo);
        com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder bo = orderMapper.selectOne(wrapper);
        return bo != null ? Optional.of(convertToAggregate(bo)) : Optional.empty();
    }

    @Override
    public List<ThirdPayOrder> findByStatusAndTimeRange(OrderStatus status, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder::getOrderStatus, status.getCode())
               .between(com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder::getInsertTime, startTime, endTime);
        List<com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder> boList = orderMapper.selectList(wrapper);
        return convertToAggregateList(boList);
    }

    @Override
    public List<ThirdPayOrder> findExpiredOrders(LocalDateTime currentTime, int timeoutMinutes) {
        LocalDateTime expireTime = currentTime.minusMinutes(timeoutMinutes);
        LambdaQueryWrapper<com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder::getOrderStatus, OrderStatus.STATUS_ACCEPT.getCode())
               .lt(com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder::getInsertTime, expireTime);
        List<com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder> boList = orderMapper.selectList(wrapper);
        return convertToAggregateList(boList);
    }

    @Override
    public boolean updateOrderStatus(OrderNo orderNo, OrderStatus status, LocalDateTime finishTime) {
        LambdaUpdateWrapper<com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder::getOrderNo, orderNo.getValue())
               .set(com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder::getOrderStatus, status.getCode())
               .set(com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder::getUpdateTime, LocalDateTime.now())
               .set(com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder::getFinishTime, finishTime);
        return orderMapper.update(null, wrapper) > 0;
    }

    @Override
    public boolean deleteByOrderNo(OrderNo orderNo) {
        LambdaQueryWrapper<com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder::getOrderNo, orderNo.getValue());
        return orderMapper.delete(wrapper) > 0;
    }

    @Override
    public OrderNo generateOrderNo() {
        // 使用与原系统相同的订单号生成逻辑
        String orderNo = generateOrderNoInternal();
        return OrderNo.of(orderNo);
    }

    private String generateOrderNoInternal() {
        // 简单的订单号生成逻辑：年月日时分秒+6位随机数
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%06d", (int)(Math.random() * 1000000));
        return timestamp + random;
    }

    // 转换方法
    private com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder convertToBo(ThirdPayOrder aggregate) {
        com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder bo = new com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder();
        BeanUtils.copyProperties(aggregate, bo);
        
        // 特殊字段转换
        bo.setOrderNo(aggregate.getOrderNo().getValue());
        bo.setThirdPayerNo(aggregate.getThirdPayerNo().getValue());
        bo.setThirdPayeeNo(aggregate.getThirdPayeeNo().getValue());
        bo.setOrderType(aggregate.getOrderType().getCode());
        bo.setOrderStatus(aggregate.getOrderStatus().getCode());
        bo.setAmt(aggregate.getAmt().getAmount());
        
        return bo;
    }

    private ThirdPayOrder convertToAggregate(com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder bo) {
        return new ThirdPayOrder.Builder()
                .setOrderId(bo.getOrderId())
                .setOrderNo(OrderNo.of(bo.getOrderNo()))
                .setThirdPayerNo(AccountNo.of(bo.getThirdPayerNo()))
                .setThirdPayeeNo(AccountNo.of(bo.getThirdPayeeNo()))
                .setMerchantPayerNo(bo.getMerchantPayerNo())
                .setMerchantPayeeNo(bo.getMerchantPayeeNo())
                .setOrderType(com.xiaoke1256.thirdpay.domain.model.valueobject.OrderType.fromCode(bo.getOrderType()))
                .setOrderStatus(com.xiaoke1256.thirdpay.domain.model.valueobject.OrderStatus.fromCode(bo.getOrderStatus()))
                .setAmt(com.xiaoke1256.thirdpay.domain.model.valueobject.Money.of(bo.getAmt()))
                .setMerchantNo(bo.getMerchantNo())
                .setMerchantOrderNo(bo.getMerchantOrderNo())
                .setBussinessNo(bo.getBussinessNo())
                .setIncident(bo.getIncident())
                .setRemark(bo.getRemark())
                .setInsertTime(bo.getInsertTime())
                .setUpdateTime(bo.getUpdateTime())
                .setFinishTime(bo.getFinishTime())
                .build();
    }

    private List<ThirdPayOrder> convertToAggregateList(List<com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder> boList) {
        List<ThirdPayOrder> aggregateList = new ArrayList<>();
        for (com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder bo : boList) {
            aggregateList.add(convertToAggregate(bo));
        }
        return aggregateList;
    }
}