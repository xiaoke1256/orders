package com.xiaoke1256.thirdpay.domain.service;

import com.xiaoke1256.thirdpay.domain.event.DomainEventPublisher;
import com.xiaoke1256.thirdpay.domain.event.OrderFailedEvent;
import com.xiaoke1256.thirdpay.domain.event.OrderPaidEvent;
import com.xiaoke1256.thirdpay.domain.model.aggregate.HouseholdAcc;
import com.xiaoke1256.thirdpay.domain.model.aggregate.Merchant;
import com.xiaoke1256.thirdpay.domain.model.aggregate.ThirdPayOrder;
import com.xiaoke1256.thirdpay.domain.model.repository.HouseholdAccRepository;
import com.xiaoke1256.thirdpay.domain.model.repository.MerchantRepository;
import com.xiaoke1256.thirdpay.domain.model.repository.ThirdPayOrderRepository;
import com.xiaoke1256.thirdpay.domain.model.valueobject.AccountNo;
import com.xiaoke1256.thirdpay.domain.model.valueobject.Money;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderNo;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付领域服务
 */
@Service
public class PayService {
    
    @Autowired
    private ThirdPayOrderRepository orderRepository;

    @Autowired
    private HouseholdAccRepository accountRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private DomainEventPublisher eventPublisher;
    
    /**
     * 处理支付请求
     * @param merchantNo 商户编号
     * @param merchantOrderNo 商户订单号
     * @param payerAccNo 付款方账号
     * @param payeeAccNo 收款方账号
     * @param amt 金额
     * @param orderType 订单类型
     * @param bussinessNo 业务编号
     * @param incident 事件
     * @param remark 备注
     * @return 创建的订单
     */
    @Transactional
    public ThirdPayOrder processPayment(String merchantNo, String merchantOrderNo, 
                                       AccountNo payerAccNo, AccountNo payeeAccNo,
                                       Money amt, OrderType orderType,
                                       String bussinessNo, String incident, String remark) {
        // 验证商户
        Merchant merchant = merchantRepository.findByMerchantNo(merchantNo)
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found: " + merchantNo));
        
        if (!merchant.isActive()) {
            throw new IllegalStateException("Merchant is not active: " + merchantNo);
        }
        
        // 检查是否已存在相同的商户订单号
        if (orderRepository.findByMerchantOrderNo(merchantNo, merchantOrderNo).isPresent()) {
            throw new IllegalStateException("Order already exists for merchant order no: " + merchantOrderNo);
        }
        
        // 检查付款方账户余额
        HouseholdAcc payerAccount = accountRepository.findByAccNo(payerAccNo)
                .orElseThrow(() -> new IllegalArgumentException("Payer account not found: " + payerAccNo.getValue()));
        
        if (!payerAccount.hasSufficientBalance(amt)) {
            throw new IllegalStateException("Insufficient balance for account: " + payerAccNo.getValue());
        }
        
        // 检查收款方账户是否存在
        HouseholdAcc payeeAccount = accountRepository.findByAccNo(payeeAccNo)
                .orElseThrow(() -> new IllegalArgumentException("Payee account not found: " + payeeAccNo.getValue()));
        
        // 生成订单号
        OrderNo orderNo = orderRepository.generateOrderNo();
        
        // 创建订单
        ThirdPayOrder order = new ThirdPayOrder.Builder()
                .setOrderNo(orderNo)
                .setThirdPayerNo(payerAccNo)
                .setThirdPayeeNo(payeeAccNo)
                .setMerchantNo(merchantNo)
                .setMerchantOrderNo(merchantOrderNo)
                .setOrderType(orderType)
                .setAmt(amt)
                .setBussinessNo(bussinessNo)
                .setIncident(incident)
                .setRemark(remark)
                .build();
        
        // 保存订单
        order = orderRepository.save(order);
        
        // 执行账务操作
        performAccounting(payerAccount, payeeAccount, amt, orderNo);
        
        return order;
    }
    
    /**
     * 执行账务操作
     * @param payerAccount 付款方账户
     * @param payeeAccount 收款方账户
     * @param amt 金额
     * @param orderNo 订单号
     */
    @Transactional
    private void performAccounting(HouseholdAcc payerAccount, HouseholdAcc payeeAccount, 
                                  Money amt, OrderNo orderNo) {
        // 扣款
        payerAccount.debit(amt);
        accountRepository.save(payerAccount);
        
        // 收款
        payeeAccount.credit(amt);
        accountRepository.save(payeeAccount);
    }
    
    /**
     * 处理订单支付成功
     * @param orderNo 订单号
     * @return 处理后的订单
     */
    @Transactional
    public ThirdPayOrder processOrderSuccess(OrderNo orderNo) {
        ThirdPayOrder order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderNo.getValue()));
        
        order.markAsSuccess();
        ThirdPayOrder updatedOrder = orderRepository.save(order);
        
        // 发布订单支付成功事件
        eventPublisher.publish(new OrderPaidEvent(updatedOrder.getOrderNo(), updatedOrder));
        
        return updatedOrder;
    }
    
    /**
     * 处理订单支付失败
     * @param orderNo 订单号
     * @return 处理后的订单
     */
    @Transactional
    public ThirdPayOrder processOrderFail(OrderNo orderNo, String reason) {
        ThirdPayOrder order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderNo.getValue()));
        
        order.markAsFail();
        ThirdPayOrder updatedOrder = orderRepository.save(order);
        
        // 发布订单支付失败事件
        eventPublisher.publish(new OrderFailedEvent(updatedOrder.getOrderNo(), updatedOrder, reason != null ? reason : "支付失败"));
        
        return updatedOrder;
    }
    
    // 保留原有的无参数reason的方法作为兼容
    @Transactional
    public ThirdPayOrder processOrderFail(OrderNo orderNo) {
        return processOrderFail(orderNo, null);
    }
    
    /**
     * 处理超时订单
     * @param timeoutMinutes 超时分钟数
     */
    @Transactional
    public void processExpiredOrders(int timeoutMinutes) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<ThirdPayOrder> expiredOrders = orderRepository.findExpiredOrders(currentTime, timeoutMinutes);
        
        for (ThirdPayOrder order : expiredOrders) {
            order.markAsExpired();
            ThirdPayOrder updatedOrder = orderRepository.save(order);
            
            // 发布订单过期事件
            eventPublisher.publish(new OrderFailedEvent(updatedOrder.getOrderNo(), updatedOrder, "订单超时未支付"));
        }
    }
}