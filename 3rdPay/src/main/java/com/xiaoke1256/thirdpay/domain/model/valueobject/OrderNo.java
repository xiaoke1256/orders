package com.xiaoke1256.thirdpay.domain.model.valueobject;

import java.util.Date;
import java.util.Random;

/**
 * 订单号值对象
 */
public class OrderNo {
    private final String value;
    private static final Random RANDOM = new Random();

    private OrderNo(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Order number cannot be null or empty");
        }
        // 验证订单号格式：年月日时分秒 + 6位流水号
        if (!value.matches("\\d{14}\\d{6}")) {
            throw new IllegalArgumentException("Invalid order number format");
        }
        this.value = value;
    }

    public static OrderNo of(String value) {
        return new OrderNo(value);
    }

    /**
     * 生成新的订单号
     * 订单号规则:年月日时分秒+6位流水号
     */
    public static OrderNo generate() {
        StringBuilder sb = new StringBuilder();
        // 添加年月日时分秒
        sb.append(String.format("%tY%<tm%<td%<tH%<tM%<tS", new Date()));
        // 添加6位随机数
        sb.append(String.format("%06d", RANDOM.nextInt(1000000)));
        return new OrderNo(sb.toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OrderNo orderNo = (OrderNo) obj;
        return value.equals(orderNo.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}