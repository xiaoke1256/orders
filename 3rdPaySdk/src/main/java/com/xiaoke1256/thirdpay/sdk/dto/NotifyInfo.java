package com.xiaoke1256.thirdpay.sdk.dto;

import lombok.Data;

/**
 * 支付完成后，订单通知信息
 * @author xuke
 *
 */
@Data
public class NotifyInfo {
    private String orderNo;
    private String status;
    private String merchantOrderNo;
    private String bussinessNo;
    private String resultCode;
    private String msg;
    private Long timestamp;
    private String sign;
}
