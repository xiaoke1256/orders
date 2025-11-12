package com.xiaoke1256.thirdpay.payplatform.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayFormInfo {
    private String payerNo;//付款方账号
    private String payeeNo;//收款方账号
    private String payerName;//付款方名
    private String payeeName;//收款方名

    private String merchantPayerNo;
    private String merchantPayeeNo;
    private String orderType;
    private String payType;
    private BigDecimal amt;
    private String merchantNo;
    private String merchantOrderNo;
    private String incident;
    private String remark;
    private String bussinessNo;
    /** 过期时间 */
    private Long expiredTime;

    private String successCallbackUrl;
    private String failCallbackUrl;
}
