package com.xiaoke1256.thirdpay.payplatform.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商户表
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-10-31
 */
@Getter
@Setter
@TableName("merchant")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户主键
     */
    @TableId(value = "merchant_id", type = IdType.AUTO)
    private Long merchantId;

    /**
     * 商户编号
     */
    @TableField("merchant_no")
    private String merchantNo;

    /**
     * 商户名称
     */
    @TableField("merchant_name")
    private String merchantName;

    /**
     * 商户收款账号
     */
    @TableField("default_acc_no")
    private String defaultAccNo;

    /**
     * 插入时间
     */
    @TableField("insert_time")
    private LocalDateTime insertTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
