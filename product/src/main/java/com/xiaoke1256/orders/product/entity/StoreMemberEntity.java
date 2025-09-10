package com.xiaoke1256.orders.product.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 店员表
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-10
 */
@Getter
@Setter
@TableName("store_member")
public class StoreMemberEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "store_member_id", type = IdType.AUTO)
    private Long storeMemberId;

    /**
     * 商铺编号
     */
    private String storeNo;

    /**
     * 会员账号（登录时用）
     */
    private String accountNo;

    /**
     * 角色（1：店长；2：店员）
     */
    private String role;

    /**
     * 是否默认商店
     */
    private String isDefaultStore;

    /**
     * 创建时间
     */
    @TableField(fill= FieldFill.INSERT)
    private Date insertTime;

    /**
     * 修改时间
     */
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
