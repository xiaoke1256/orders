package com.xiaoke1256.orders.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品参数
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-08
 */
@Getter
@Setter
@TableName("product_param")
public class ProductParamEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "param_id", type = IdType.AUTO)
    private Long paramId;

    /**
     * 商品编号，外键
     */
    private String productCode;

    /**
     * 参数名
     */
    private String paramName;

    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 参数描述
     */
    private String paramDesc;

    /**
     * 显示顺序
     */
    private Integer showOrder;
}
