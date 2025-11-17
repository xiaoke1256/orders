package com.xiaoke1256.thirdpay.domain.model.valueobject;

/**
 * 订单状态枚举值对象
 */
public enum OrderStatus {
    STATUS_ACCEPT("00", "受理支付"),
    STATUS_FAIL("99", "失败"),
    STATUS_SUCCESS("90", "成功"),
    STATUS_EXPIRED("98", "处理超时"),
    STATUS_NEED_MANNUAL("95", "需人工处理");

    private final String code;
    private final String desc;

    OrderStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static OrderStatus fromCode(String code) {
        for (OrderStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid order status code: " + code);
    }
}