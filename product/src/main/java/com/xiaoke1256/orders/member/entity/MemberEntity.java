package com.xiaoke1256.orders.member.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-10
 */
@Getter
@Setter
@TableName("member")
public class MemberEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员主键
     */
    @TableId(value = "member_id", type = IdType.AUTO)
    private Long memberId;

    /**
     * 会员账号（登录时用）
     */
    private String accountNo;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别：M 男；F 女
     */
    private String gender;

    /**
     * 自我介绍
     */
    private String intro;

    /**
     * 绑定邮箱
     */
    private String email;

    /**
     * 移动电话号
     */
    private String mobilePhone;

    /**
     * 密码
     */
    private String password;

    /**
     * 插入时间
     */
    @TableField(fill= FieldFill.INSERT)
    private LocalDateTime insertTime;

    /**
     * 修改时间
     */
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
