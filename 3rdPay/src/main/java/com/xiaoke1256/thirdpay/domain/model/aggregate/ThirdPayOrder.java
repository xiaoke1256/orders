package com.xiaoke1256.thirdpay.domain.model.aggregate;

import com.xiaoke1256.thirdpay.domain.model.valueobject.AccountNo;
import com.xiaoke1256.thirdpay.domain.model.valueobject.Money;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderNo;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderStatus;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderType;

import java.time.LocalDateTime;

/**
 * 第三方支付订单聚合根
 */
public class ThirdPayOrder {

    private Long orderId;
    private final OrderNo orderNo;
    private final AccountNo thirdPayerNo;
    private final AccountNo thirdPayeeNo;
    private final String merchantPayerNo;
    private final String merchantPayeeNo;
    private final OrderType orderType;
    private OrderStatus orderStatus;
    private final Money amt;
    private final String merchantNo;
    private final String merchantOrderNo;
    private final String bussinessNo;
    private final String incident;
    private final String remark;
    private final LocalDateTime insertTime;
    private LocalDateTime updateTime;
    private LocalDateTime finishTime;

    private ThirdPayOrder(Builder builder) {
        this.orderId = builder.orderId;
        this.orderNo = builder.orderNo;
        this.thirdPayerNo = builder.thirdPayerNo;
        this.thirdPayeeNo = builder.thirdPayeeNo;
        this.merchantPayerNo = builder.merchantPayerNo;
        this.merchantPayeeNo = builder.merchantPayeeNo;
        this.orderType = builder.orderType;
        this.orderStatus = builder.orderStatus;
        this.amt = builder.amt;
        this.merchantNo = builder.merchantNo;
        this.merchantOrderNo = builder.merchantOrderNo;
        this.bussinessNo = builder.bussinessNo;
        this.incident = builder.incident;
        this.remark = builder.remark;
        this.insertTime = builder.insertTime;
        this.updateTime = builder.updateTime;
        this.finishTime = builder.finishTime;
    }

    public static class Builder {
        private Long orderId;
        private OrderNo orderNo;
        private AccountNo thirdPayerNo;
        private AccountNo thirdPayeeNo;
        private String merchantPayerNo;
        private String merchantPayeeNo;
        private OrderType orderType;
        private OrderStatus orderStatus = OrderStatus.STATUS_ACCEPT;
        private Money amt;
        private String merchantNo;
        private String merchantOrderNo;
        private String bussinessNo;
        private String incident;
        private String remark;
        private LocalDateTime insertTime = LocalDateTime.now();
        private LocalDateTime updateTime = LocalDateTime.now();
        private LocalDateTime finishTime;

        public Builder setOrderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder setOrderNo(OrderNo orderNo) {
            this.orderNo = orderNo;
            return this;
        }

        public Builder setThirdPayerNo(AccountNo thirdPayerNo) {
            this.thirdPayerNo = thirdPayerNo;
            return this;
        }

        public Builder setThirdPayeeNo(AccountNo thirdPayeeNo) {
            this.thirdPayeeNo = thirdPayeeNo;
            return this;
        }

        public Builder setMerchantPayerNo(String merchantPayerNo) {
            this.merchantPayerNo = merchantPayerNo;
            return this;
        }

        public Builder setMerchantPayeeNo(String merchantPayeeNo) {
            this.merchantPayeeNo = merchantPayeeNo;
            return this;
        }

        public Builder setOrderType(OrderType orderType) {
            this.orderType = orderType;
            return this;
        }

        public Builder setOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder setAmt(Money amt) {
            this.amt = amt;
            return this;
        }

        public Builder setMerchantNo(String merchantNo) {
            this.merchantNo = merchantNo;
            return this;
        }

        public Builder setMerchantOrderNo(String merchantOrderNo) {
            this.merchantOrderNo = merchantOrderNo;
            return this;
        }

        public Builder setBussinessNo(String bussinessNo) {
            this.bussinessNo = bussinessNo;
            return this;
        }

        public Builder setIncident(String incident) {
            this.incident = incident;
            return this;
        }

        public Builder setRemark(String remark) {
            this.remark = remark;
            return this;
        }

        public Builder setInsertTime(LocalDateTime insertTime) {
            this.insertTime = insertTime;
            return this;
        }

        public Builder setUpdateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder setFinishTime(LocalDateTime finishTime) {
            this.finishTime = finishTime;
            return this;
        }

        public ThirdPayOrder build() {
            validate();
            return new ThirdPayOrder(this);
        }

        private void validate() {
            if (orderNo == null) {
                throw new IllegalArgumentException("Order number cannot be null");
            }
            if (thirdPayerNo == null) {
                throw new IllegalArgumentException("Third payer number cannot be null");
            }
            if (thirdPayeeNo == null) {
                throw new IllegalArgumentException("Third payee number cannot be null");
            }
            if (orderType == null) {
                throw new IllegalArgumentException("Order type cannot be null");
            }
            if (amt == null) {
                throw new IllegalArgumentException("Amount cannot be null");
            }
            if (merchantNo == null) {
                throw new IllegalArgumentException("Merchant number cannot be null");
            }
        }
    }

    // 业务行为

    /**
     * 标记订单支付成功
     */
    public void markAsSuccess() {
        if (!orderStatus.equals(OrderStatus.STATUS_ACCEPT)) {
            throw new IllegalStateException("Order is not in accepted status");
        }
        this.orderStatus = OrderStatus.STATUS_SUCCESS;
        this.finishTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 标记订单支付失败
     */
    public void markAsFail() {
        if (!orderStatus.equals(OrderStatus.STATUS_ACCEPT)) {
            throw new IllegalStateException("Order is not in accepted status");
        }
        this.orderStatus = OrderStatus.STATUS_FAIL;
        this.finishTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 标记订单过期
     */
    public void markAsExpired() {
        if (!orderStatus.equals(OrderStatus.STATUS_ACCEPT)) {
            throw new IllegalStateException("Order is not in accepted status");
        }
        this.orderStatus = OrderStatus.STATUS_EXPIRED;
        this.finishTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 标记订单需人工处理
     */
    public void markAsNeedManualProcess() {
        if (!orderStatus.equals(OrderStatus.STATUS_EXPIRED)) {
            throw new IllegalStateException("Order is not in expired status");
        }
        this.orderStatus = OrderStatus.STATUS_NEED_MANNUAL;
        this.finishTime = null;
        this.updateTime = LocalDateTime.now();
    }

    // Getters

    public Long getOrderId() {
        return orderId;
    }

    public OrderNo getOrderNo() {
        return orderNo;
    }

    public AccountNo getThirdPayerNo() {
        return thirdPayerNo;
    }

    public AccountNo getThirdPayeeNo() {
        return thirdPayeeNo;
    }

    public String getMerchantPayerNo() {
        return merchantPayerNo;
    }

    public String getMerchantPayeeNo() {
        return merchantPayeeNo;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Money getAmt() {
        return amt;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public String getBussinessNo() {
        return bussinessNo;
    }

    public String getIncident() {
        return incident;
    }

    public String getRemark() {
        return remark;
    }

    public LocalDateTime getInsertTime() {
        return insertTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}