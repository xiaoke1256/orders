package com.xiaoke1256.orders.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品附加选项
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-08
 */
@Getter
@Setter
@TableName("product_attached_option")
public class ProductAttachedOptionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 选项编码，主键
     */
    private String optionCode;

    /**
     * 商品编号，外键
     */
    private String productCode;

    /**
     * 选项类型：1=尺码，2=颜色，3=套装，4=型号，9=其他
     */
    private String optionType;

    /**
     * 选项名称
     */
    private String optionName;

    /**
     * 选项值，类型选颜色时，可以将颜色的编码值，保存在此。
     */
    private String optionValue;

    /**
     * 价格，指附加选项以后的价格
     */
    private BigDecimal price;

    /**
     * 显示顺序
     */
    private Integer showOrder;
}
