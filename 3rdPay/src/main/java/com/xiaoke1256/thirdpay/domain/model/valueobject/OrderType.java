package com.xiaoke1256.thirdpay.domain.model.valueobject;

/**
 * 订单类型枚举值对象
 */
public enum OrderType {
    TYPE_CONSUME("01", "消费"),
    TYPE_REFUND("02", "退货款"),
    TYPE_SETTLE("03", "与平台方结算"),
    TYPE_FINANCE("04", "理财"),
    TYPE_INTEREST("05", "结息"),
    TYPE_LOAN("06", "借款"),
    TYPE_REPAY("07", "还款"),
    TYPE_OTHER("99", "其他");

    private final String code;
    private final String desc;

    OrderType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static OrderType fromCode(String code) {
        for (OrderType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid order type code: " + code);
    }
}