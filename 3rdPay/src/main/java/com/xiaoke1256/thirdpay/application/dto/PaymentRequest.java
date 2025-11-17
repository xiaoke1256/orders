package com.xiaoke1256.thirdpay.application.dto;

import java.math.BigDecimal;

/**
 * 支付请求DTO
 */
public class PaymentRequest {
    
    /**
     * 商户编号
     */
    private String merchantNo;
    
    /**
     * 支付金额
     */
    private BigDecimal amount;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 订单类型
     */
    private String orderType;
    
    /**
     * 客户端IP
     */
    private String clientIp;
    
    /**
     * 回调URL
     */
    private String notifyUrl;
    
    /**
     * 备注
     */
    private String remark;
    
    // getter and setter
    public String getMerchantNo() {
        return merchantNo;
    }
    
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getOrderType() {
        return orderType;
    }
    
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
    
    public String getClientIp() {
        return clientIp;
    }
    
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    
    public String getNotifyUrl() {
        return notifyUrl;
    }
    
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
}