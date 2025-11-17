package com.xiaoke1256.thirdpay.application.service.impl;

import com.xiaoke1256.thirdpay.application.dto.PaymentRequest;
import com.xiaoke1256.thirdpay.application.dto.PaymentResponse;
import com.xiaoke1256.thirdpay.application.service.ThirdPayApplicationService;
import com.xiaoke1256.thirdpay.domain.model.aggregate.Merchant;
import com.xiaoke1256.thirdpay.domain.model.aggregate.ThirdPayOrder;
import com.xiaoke1256.thirdpay.domain.model.valueobject.AccountNo;
import com.xiaoke1256.thirdpay.domain.model.valueobject.MerchantNo;
import com.xiaoke1256.thirdpay.domain.model.valueobject.Money;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderNo;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderType;
import com.xiaoke1256.thirdpay.domain.model.repository.ThirdPayOrderRepository;
import com.xiaoke1256.thirdpay.domain.model.repository.MerchantRepository;
import com.xiaoke1256.thirdpay.domain.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 第三方支付应用服务实现类
 */
@Service
@Transactional
public class ThirdPayApplicationServiceImpl implements ThirdPayApplicationService {
    
    private static final Logger logger = LoggerFactory.getLogger(ThirdPayApplicationServiceImpl.class);
    
    @Autowired
    private PayService payService;
    
    @Autowired
    private ThirdPayOrderRepository orderRepository;
    
    @Autowired
    private MerchantRepository merchantRepository;
    
    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        logger.info("创建支付订单，商户编号: {}", request.getMerchantNo());
        
        // 1. 验证商户信息
        MerchantNo merchantNo = MerchantNo.of(request.getMerchantNo());//new MerchantNo(request.getMerchantNo());
        Optional<Merchant> merchantOptional = merchantRepository.findByMerchantNo(merchantNo.getValue());
        if (!merchantOptional.isPresent()) {
            PaymentResponse response = new PaymentResponse();
            response.setErrorCode("MERCHANT_NOT_FOUND");
            response.setErrorMsg("商户不存在");
            return response;
        }
        
        // 2. 构建订单对象
        OrderNo orderNo = OrderNo.generate(); // 生成订单号
        OrderType orderType = OrderType.fromCode(request.getOrderType()); // 获取订单类型
        Money amt = Money.of(request.getAmount());// 创建金额对象
        
        ThirdPayOrder order = new ThirdPayOrder.Builder()
            .setOrderNo(orderNo)
            .setThirdPayerNo(AccountNo.of(request.getClientIp())) // 使用客户端IP作为第三方支付方编号
            .setThirdPayeeNo(AccountNo.of(merchantNo.getValue())) // 使用商户编号作为第三方收款方编号
            .setMerchantPayerNo(request.getClientIp())
            .setMerchantPayeeNo(merchantNo.getValue())
            .setOrderType(orderType)
            .setAmt(amt)
            .setMerchantNo(merchantNo.getValue())
            .setMerchantOrderNo(request.getDescription()) // 使用描述作为商户订单号
            .setBussinessNo(request.getNotifyUrl()) // 使用通知URL作为业务编号
            .setIncident(request.getRemark())
            .setRemark(request.getRemark())
            .build();
        
        // 3. 保存订单
        orderRepository.save(order);
        
        // 4. 返回响应
        PaymentResponse response = new PaymentResponse();
        response.setOrderNo(order.getOrderNo().getValue());
        response.setAmount(order.getAmt().getValue());
        response.setStatus(order.getOrderStatus().getCode());
        
        // 生成支付链接（如果需要）
        response.setPaymentUrl(generatePaymentUrl(order.getOrderNo()));
        
        return response;
    }
    
    @Override
    public ThirdPayOrder queryOrder(OrderNo orderNo) {
        logger.info("查询订单，订单号: {}", orderNo.getValue());
        return orderRepository.findByOrderNo(orderNo)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderNo.getValue()));
    }
    
    @Override
    @Transactional
    public void handlePaymentCallback(OrderNo orderNo, String status, String transactionNo) {
        logger.info("处理支付回调，订单号: {}, 状态: {}, 交易号: {}", 
            orderNo.getValue(), status, transactionNo);
        
        // 1. 查询订单
        ThirdPayOrder order = orderRepository.findByOrderNo(orderNo)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderNo.getValue()));
        
        // 2. 根据状态处理订单
        if ("SUCCESS".equals(status)) {
            // 处理支付成功
            payService.processOrderSuccess(order.getOrderNo());
        } else if ("FAILED".equals(status) || "CLOSED".equals(status)) {
            // 处理支付失败
            payService.processOrderFail(order.getOrderNo(), status);
        }
    }
    
    @Override
    @Transactional
    public void closeExpiredOrders() {
        logger.info("关闭过期订单");
        payService.processExpiredOrders(30); // 默认过期时间30分钟
    }
    
    /**
     * 生成支付链接
     */
    private String generatePaymentUrl(OrderNo orderNo) {
        // 根据实际业务需求生成支付链接
        return "/api/pay?orderNo=" + orderNo.getValue();
    }
}