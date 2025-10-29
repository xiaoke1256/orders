package com.xiaoke1256.thirdpay.sdk.vo;

import com.xiaoke1256.thirdpay.sdk.dto.OrderInfo;
import lombok.Data;

@Data
public class OrderFormInfo {
    private OrderInfo orderInfo;
    private String sign;//对象orderInfo进行签名
    /** 过期时间 */
    private Long expiredTime;
}
