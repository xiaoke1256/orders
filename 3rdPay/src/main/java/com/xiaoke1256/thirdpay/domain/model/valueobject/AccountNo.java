package com.xiaoke1256.thirdpay.domain.model.valueobject;

/**
 * 账号值对象
 */
public class AccountNo {
    private final String value;

    private AccountNo(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }
        // 验证账号格式：18位数字
        if (!value.matches("\\d{18}")) {
            throw new IllegalArgumentException("Invalid account number format, must be 18 digits");
        }
        this.value = value;
    }

    public static AccountNo of(String value) {
        return new AccountNo(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccountNo accountNo = (AccountNo) obj;
        return value.equals(accountNo.value);
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