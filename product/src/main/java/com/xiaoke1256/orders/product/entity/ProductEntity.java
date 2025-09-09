package com.xiaoke1256.orders.product.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.xiaoke1256.orders.product.bo.ProductParam;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-07
 */
@Getter
@Setter
@TableName("product")
public class ProductEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品编号，主键
     */
    @TableId
    private String productCode;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 价格(厘)
     */
    private BigDecimal productPrice;

    /**
     * 店铺号
     */
    private String storeNo;

    /**
     * 状态：0=下架，1=上架
     */
    private String productStatus;

    /**
     * 上架时间，用于搜索引擎采集过滤(废除，靠update_time字段判断)
     */
    private LocalDateTime onSaleTime;

    /**
     * 简介
     */
    private String productIntro;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 插入时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime insertTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否正在进行秒杀活动。（是:1;否:0）
     */
    private String inSeckill;

    @TableField(exist = false)
    private StoreEntity store;

    @TableField(exist = false)
    private List<ProductTypeEntity> productTypes;

//    private List<ProductParamEntity> params;
}
