package com.xiaoke1256.thirdpay.domain.model.aggregate;

import java.time.LocalDateTime;

/**
 * 商户聚合根
 */
public class Merchant {
    private Long merchantId;
    private final String merchantNo;
    private final String merchantName;
    private final String publicKey;
    private final String privateKey;
    private final String notifyUrl;
    private final String successReturnUrl;
    private final String failReturnUrl;
    private final String contactName;
    private final String contactPhone;
    private final Integer status;
    private final LocalDateTime insertTime;
    private LocalDateTime updateTime;

    private Merchant(Builder builder) {
        this.merchantId = builder.merchantId;
        this.merchantNo = builder.merchantNo;
        this.merchantName = builder.merchantName;
        this.publicKey = builder.publicKey;
        this.privateKey = builder.privateKey;
        this.notifyUrl = builder.notifyUrl;
        this.successReturnUrl = builder.successReturnUrl;
        this.failReturnUrl = builder.failReturnUrl;
        this.contactName = builder.contactName;
        this.contactPhone = builder.contactPhone;
        this.status = builder.status;
        this.insertTime = builder.insertTime;
        this.updateTime = builder.updateTime;
    }

    public static class Builder {
        private Long merchantId;
        private String merchantNo;
        private String merchantName;
        private String publicKey;
        private String privateKey;
        private String notifyUrl;
        //private String returnUrl;
        private String successReturnUrl;
        private String failReturnUrl;

        private String contactName;
        private String contactPhone;
        private Integer status = 1;
        private LocalDateTime insertTime = LocalDateTime.now();
        private LocalDateTime updateTime = LocalDateTime.now();

        public Builder setMerchantId(Long merchantId) {
            this.merchantId = merchantId;
            return this;
        }

        public Builder setMerchantNo(String merchantNo) {
            this.merchantNo = merchantNo;
            return this;
        }

        public Builder setMerchantName(String merchantName) {
            this.merchantName = merchantName;
            return this;
        }

        public Builder setPublicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public Builder setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
            return this;
        }

        public Builder setNotifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
            return this;
        }

        public Builder setSuccessReturnUrl(String successReturnUrl) {
            this.successReturnUrl = successReturnUrl;
            return this;
        }

        public Builder setFailReturnUrl(String failReturnUrl) {
            this.failReturnUrl = failReturnUrl;
            return this;
        }

        public Builder setContactName(String contactName) {
            this.contactName = contactName;
            return this;
        }

        public Builder setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
            return this;
        }

        public Builder setStatus(Integer status) {
            this.status = status;
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

        public Merchant build() {
            validate();
            return new Merchant(this);
        }

        private void validate() {
            if (merchantNo == null || merchantNo.trim().isEmpty()) {
                throw new IllegalArgumentException("Merchant number cannot be null or empty");
            }
            if (merchantName == null || merchantName.trim().isEmpty()) {
                throw new IllegalArgumentException("Merchant name cannot be null or empty");
            }
            if (publicKey == null || publicKey.trim().isEmpty()) {
                throw new IllegalArgumentException("Public key cannot be null or empty");
            }
        }
    }

    // 业务行为

    /**
     * 检查商户是否可用
     * @return 商户是否可用
     */
    public boolean isActive() {
        return status != null && status == 1;
    }

    // Getters

    public Long getMerchantId() {
        return merchantId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getSuccessReturnUrl() {
        return successReturnUrl;
    }

    public String getFailReturnUrl() {
        return failReturnUrl;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public Integer getStatus() {
        return status;
    }

    public LocalDateTime getInsertTime() {
        return insertTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }
}