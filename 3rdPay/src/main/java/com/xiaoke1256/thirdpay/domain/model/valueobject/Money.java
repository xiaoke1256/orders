package com.xiaoke1256.thirdpay.domain.model.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 金额值对象
 * 统一的金额表示和操作类
 */
public class Money {
    private final BigDecimal amount;
    private static final int SCALE = 2;

    private Money(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount.setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money of(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money of(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }
    
    /**
     * 从字符串创建金额对象
     * @param value 金额字符串
     * @return 金额对象
     */
    public static Money of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Amount string cannot be empty");
        }
        return new Money(new BigDecimal(value));
    }
    
    /**
     * 从整型创建金额对象（单位：分）
     * @param value 金额（分）
     * @return 金额对象
     */
    public static Money ofCent(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Cent amount cannot be negative");
        }
        return new Money(BigDecimal.valueOf(value).divide(BigDecimal.valueOf(100)));
    }

    public BigDecimal getAmount() {
        return amount;
    }
    
    /**
     * 获取金额值（别名方法，兼容其他使用场景）
     * @return 金额值
     */
    public BigDecimal getValue() {
        return amount;
    }
    
    /**
     * 获取金额的整数表示（分）
     * @return 金额（分）
     */
    public int getCent() {
        return amount.multiply(BigDecimal.valueOf(100)).intValue();
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        if (this.amount.compareTo(other.amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        return new Money(this.amount.subtract(other.amount));
    }

    public boolean isGreaterThan(Money other) {
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return this.amount.compareTo(other.amount) >= 0;
    }

    public boolean isLessThan(Money other) {
        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money money = (Money) obj;
        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return amount.toString();
    }
}