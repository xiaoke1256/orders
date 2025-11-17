package com.xiaoke1256.thirdpay.application.service;

import com.xiaoke1256.thirdpay.domain.model.aggregate.ThirdPayOrder;
import com.xiaoke1256.thirdpay.domain.model.valueobject.MerchantNo;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderNo;
import com.xiaoke1256.thirdpay.application.dto.PaymentRequest;
import com.xiaoke1256.thirdpay.application.dto.PaymentResponse;

/**
 * 第三方支付应用服务接口
 * 负责协调领域对象完成支付流程
 */
public interface ThirdPayApplicationService {
    
    /**
     * 创建支付订单
     * @param request 支付请求
     * @return 支付响应
     */
    PaymentResponse createPayment(PaymentRequest request);
    
    /**
     * 查询订单
     * @param orderNo 订单号
     * @return 第三方支付订单
     */
    ThirdPayOrder queryOrder(OrderNo orderNo);
    
    /**
     * 处理支付回调
     * @param orderNo 订单号
     * @param status 状态
     * @param transactionNo 交易号
     */
    void handlePaymentCallback(OrderNo orderNo, String status, String transactionNo);
    
    /**
     * 关闭过期订单
     */
    void closeExpiredOrders();
}