package com.xiaoke1256.thirdpay.sdk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {
    private String payerNo;
    private String payeeNo;
    private String orderType;
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
