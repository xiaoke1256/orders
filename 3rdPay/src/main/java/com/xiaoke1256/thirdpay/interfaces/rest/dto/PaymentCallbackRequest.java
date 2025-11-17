package com.xiaoke1256.thirdpay.interfaces.rest.dto;

import java.io.Serializable;

/**
 * Payment callback request DTO
 */
public class PaymentCallbackRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String orderNo;
    private String status;
    private String transactionNo;
    private String currency;
    private Integer amount;
    private String payerAccount;
    private String paymentMethod;
    private String callbackTime;
    private String signature;
    
    public String getOrderNo() {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getTransactionNo() {
        return transactionNo;
    }
    
    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public Integer getAmount() {
        return amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public String getPayerAccount() {
        return payerAccount;
    }
    
    public void setPayerAccount(String payerAccount) {
        this.payerAccount = payerAccount;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getCallbackTime() {
        return callbackTime;
    }
    
    public void setCallbackTime(String callbackTime) {
        this.callbackTime = callbackTime;
    }
    
    public String getSignature() {
        return signature;
    }
    
    public void setSignature(String signature) {
        this.signature = signature;
    }
}