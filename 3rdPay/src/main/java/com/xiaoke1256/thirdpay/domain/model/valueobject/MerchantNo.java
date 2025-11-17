package com.xiaoke1256.thirdpay.domain.model.valueobject;

import java.util.Objects;

/**
 * 商户编号值对象
 * 表示商户的唯一标识符
 */
public class MerchantNo {
    private final String value;
    
    /**
     * 私有构造函数
     * @param value 商户编号
     */
    private MerchantNo(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("商户编号不能为空");
        }
        if (value.length() > 32) {
            throw new IllegalArgumentException("商户编号长度不能超过32个字符");
        }
        // 验证商户编号格式：只能包含字母、数字、下划线
        if (!value.matches("^[a-zA-Z0-9_]+$") && !value.matches("^[0-9]+$")) {
            throw new IllegalArgumentException("商户编号只能包含字母、数字和下划线");
        }
        this.value = value.trim();
    }
    
    /**
     * 工厂方法，创建商户编号对象
     * @param value 商户编号
     * @return 商户编号对象
     */
    public static MerchantNo of(String value) {
        return new MerchantNo(value);
    }
    
    /**
     * 获取商户编号值
     * @return 商户编号
     */
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MerchantNo that = (MerchantNo) o;
        return value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "MerchantNo{" +
                "value='" + value + "'" +
                '}';
    }
}