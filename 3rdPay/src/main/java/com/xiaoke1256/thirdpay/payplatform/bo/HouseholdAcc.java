package com.xiaoke1256.thirdpay.payplatform.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 分户账
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-11-01
 */
@Getter
@Setter
@TableName("household_acc")
public class HouseholdAcc implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账户主键
     */
    @TableId(value = "acc_id", type = IdType.AUTO)
    private Long accId;

    /**
     * 账户号
     */
    @TableField("acc_no")
    private String accNo;

    /**
     * 账户名
     */
    @TableField("acc_name")
    private String accName;

    /**
     * 余额
     */
    @TableField("balance")
    private BigDecimal balance;

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
