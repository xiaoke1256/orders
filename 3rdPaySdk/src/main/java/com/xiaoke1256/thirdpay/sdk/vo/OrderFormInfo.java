package com.xiaoke1256.thirdpay.sdk.vo;

import com.xiaoke1256.thirdpay.sdk.dto.OrderInfo;
import lombok.Data;

@Data
public class OrderFormInfo {
    /** 加密后的订单信息 */
    private String orderInfo;
    private String sign;//对象orderInfo进行签名
    private String key;
    /** 过期时间 */
    private Long expiredTime;
}
