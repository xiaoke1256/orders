package com.xiaoke1256.thirdpay.domain.model.repository;

import com.xiaoke1256.thirdpay.domain.model.aggregate.ThirdPayOrder;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderNo;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 第三方支付订单仓储接口
 */
public interface ThirdPayOrderRepository {
    
    /**
     * 保存订单
     * @param order 订单聚合根
     * @return 保存后的订单聚合根
     */
    ThirdPayOrder save(ThirdPayOrder order);
    
    /**
     * 根据订单号查询订单
     * @param orderNo 订单号
     * @return 订单聚合根，如果不存在则返回Optional.empty()
     */
    Optional<ThirdPayOrder> findByOrderNo(OrderNo orderNo);
    
    /**
     * 根据商户订单号查询订单
     * @param merchantNo 商户编号
     * @param merchantOrderNo 商户订单号
     * @return 订单聚合根，如果不存在则返回Optional.empty()
     */
    Optional<ThirdPayOrder> findByMerchantOrderNo(String merchantNo, String merchantOrderNo);
    
    /**
     * 根据订单状态查询订单列表
     * @param status 订单状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    List<ThirdPayOrder> findByStatusAndTimeRange(OrderStatus status, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查询待处理的超时订单
     * @param currentTime 当前时间
     * @param timeoutMinutes 超时分钟数
     * @return 超时订单列表
     */
    List<ThirdPayOrder> findExpiredOrders(LocalDateTime currentTime, int timeoutMinutes);
    
    /**
     * 更新订单状态
     * @param orderNo 订单号
     * @param status 新状态
     * @param finishTime 完成时间
     * @return 更新成功返回true，否则返回false
     */
    boolean updateOrderStatus(OrderNo orderNo, OrderStatus status, LocalDateTime finishTime);
    
    /**
     * 删除订单
     * @param orderNo 订单号
     * @return 删除成功返回true，否则返回false
     */
    boolean deleteByOrderNo(OrderNo orderNo);
    
    /**
     * 生成订单号
     * @return 生成的订单号
     */
    OrderNo generateOrderNo();
}