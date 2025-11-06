package com.xiaoke1256.thirdpay.sdk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {
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
    /** 随机码 */
    private String random;
    /** 过期时间 */
    private Long expiredTime;
}
