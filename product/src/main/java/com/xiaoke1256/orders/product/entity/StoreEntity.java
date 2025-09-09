package com.xiaoke1256.orders.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商铺
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-09
 */
@Getter
@Setter
@TableName("store")
public class StoreEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商铺编号，主键
     */
    private String storeNo;

    /**
     * 商铺名称
     */
    private String storeName;

    /**
     * 商铺简介
     */
    private String storeIntro;

    /**
     * 插入时间
     */
    private Timestamp insertTime;

    /**
     * 修改时间
     */
    private Timestamp updateTime;

    /**
     * 支付方式，（目前全是3rdpay）
     */
    private String payType;

    /**
     * 支付账号
     */
    private String payAccountNo;
}
