package com.xiaoke1256.orders.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品分类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-08
 */
@Getter
@Setter
@TableName("product_type")
public class ProductTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类型编号，主键
     */
    private String typeId;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 父类型编号
     */
    private String parentTypeId;

    /**
     * 类型描述
     */
    private String typeDesc;

    /**
     * 显示顺序
     */
    private Integer showOrder;

    /**
     * 插入时间
     */
    private LocalDateTime insertTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
