package com.xiaoke1256.thirdpay.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付属性配置类
 * 用于管理支付模块的配置参数
 */
@Component
@ConfigurationProperties(prefix = "thirdpay")
public class PaymentProperties {
    
    /**
     * 支付超时时间（分钟）
     */
    private int paymentTimeoutMinutes = 30;
    
    /**
     * 支付成功回调地址
     */
    private String successCallbackUrl;
    
    /**
     * 支付失败回调地址
     */
    private String failCallbackUrl;
    
    /**
     * 是否启用异步通知
     */
    private boolean asyncNotifyEnabled = true;
    
    /**
     * 通知重试次数
     */
    private int notifyRetryCount = 3;
    
    /**
     * 通知重试间隔（秒）
     */
    private int notifyRetryIntervalSeconds = 60;
    
    /**
     * 微信支付配置
     */
    private WechatPayConfig wechatPay = new WechatPayConfig();
    
    /**
     * 支付宝配置
     */
    private AlipayConfig alipay = new AlipayConfig();
    
    // getter and setter
    public int getPaymentTimeoutMinutes() {
        return paymentTimeoutMinutes;
    }
    
    public void setPaymentTimeoutMinutes(int paymentTimeoutMinutes) {
        this.paymentTimeoutMinutes = paymentTimeoutMinutes;
    }
    
    public String getSuccessCallbackUrl() {
        return successCallbackUrl;
    }
    
    public void setSuccessCallbackUrl(String successCallbackUrl) {
        this.successCallbackUrl = successCallbackUrl;
    }
    
    public String getFailCallbackUrl() {
        return failCallbackUrl;
    }
    
    public void setFailCallbackUrl(String failCallbackUrl) {
        this.failCallbackUrl = failCallbackUrl;
    }
    
    public boolean isAsyncNotifyEnabled() {
        return asyncNotifyEnabled;
    }
    
    public void setAsyncNotifyEnabled(boolean asyncNotifyEnabled) {
        this.asyncNotifyEnabled = asyncNotifyEnabled;
    }
    
    public int getNotifyRetryCount() {
        return notifyRetryCount;
    }
    
    public void setNotifyRetryCount(int notifyRetryCount) {
        this.notifyRetryCount = notifyRetryCount;
    }
    
    public int getNotifyRetryIntervalSeconds() {
        return notifyRetryIntervalSeconds;
    }
    
    public void setNotifyRetryIntervalSeconds(int notifyRetryIntervalSeconds) {
        this.notifyRetryIntervalSeconds = notifyRetryIntervalSeconds;
    }
    
    public WechatPayConfig getWechatPay() {
        return wechatPay;
    }
    
    public void setWechatPay(WechatPayConfig wechatPay) {
        this.wechatPay = wechatPay;
    }
    
    public AlipayConfig getAlipay() {
        return alipay;
    }
    
    public void setAlipay(AlipayConfig alipay) {
        this.alipay = alipay;
    }
    
    /**
     * 微信支付配置内部类
     */
    public static class WechatPayConfig {
        private String appId;
        private String mchId;
        private String key;
        private String certPath;
        private String apiUrl = "https://api.mch.weixin.qq.com";
        
        // getter and setter
        public String getAppId() {
            return appId;
        }
        
        public void setAppId(String appId) {
            this.appId = appId;
        }
        
        public String getMchId() {
            return mchId;
        }
        
        public void setMchId(String mchId) {
            this.mchId = mchId;
        }
        
        public String getKey() {
            return key;
        }
        
        public void setKey(String key) {
            this.key = key;
        }
        
        public String getCertPath() {
            return certPath;
        }
        
        public void setCertPath(String certPath) {
            this.certPath = certPath;
        }
        
        public String getApiUrl() {
            return apiUrl;
        }
        
        public void setApiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
        }
    }
    
    /**
     * 支付宝配置内部类
     */
    public static class AlipayConfig {
        private String appId;
        private String merchantPrivateKey;
        private String alipayPublicKey;
        private String charset = "UTF-8";
        private String signType = "RSA2";
        private String gatewayUrl = "https://openapi.alipay.com/gateway.do";
        
        // getter and setter
        public String getAppId() {
            return appId;
        }
        
        public void setAppId(String appId) {
            this.appId = appId;
        }
        
        public String getMerchantPrivateKey() {
            return merchantPrivateKey;
        }
        
        public void setMerchantPrivateKey(String merchantPrivateKey) {
            this.merchantPrivateKey = merchantPrivateKey;
        }
        
        public String getAlipayPublicKey() {
            return alipayPublicKey;
        }
        
        public void setAlipayPublicKey(String alipayPublicKey) {
            this.alipayPublicKey = alipayPublicKey;
        }
        
        public String getCharset() {
            return charset;
        }
        
        public void setCharset(String charset) {
            this.charset = charset;
        }
        
        public String getSignType() {
            return signType;
        }
        
        public void setSignType(String signType) {
            this.signType = signType;
        }
        
        public String getGatewayUrl() {
            return gatewayUrl;
        }
        
        public void setGatewayUrl(String gatewayUrl) {
            this.gatewayUrl = gatewayUrl;
        }
    }
}