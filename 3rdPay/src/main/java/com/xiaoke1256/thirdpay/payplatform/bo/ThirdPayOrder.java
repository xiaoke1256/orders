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
 * 第三方支付记录表
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-11-06
 */
@Getter
@Setter
@TableName("third_pay_order")
public class ThirdPayOrder implements Serializable {

    private static final long serialVersionUID = 1L;


    /**状态：刚刚接收到订单*/
    public static final String STATUS_ACCEPT = "00";

    /**状态：订单处理成功*/
    public static final String STATUS_SUCCESS = "90";

    /**状态：订单处理失败*/
    public static final String STATUS_FAIL = "99";

    /**状态：订单处理超时失败*/
    public static final String STATUS_EXPIRED = "98";

    /**状态：订单处理失败，需人工处理*/
    public static final String STATUS_NEED_MANNUAL = "95";

    /**
     * 订单主键
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 付款方会员号(第三方平台的)
     */
    @TableField("third_payer_no")
    private String thirdPayerNo;

    /**
     * 收款方会员号(第三方平台的)
     */
    @TableField("third_payee_no")
    private String thirdPayeeNo;

    /**
     * 商户方的付款方账号
     */
    @TableField("merchant_payer_no")
    private String merchantPayerNo;

    /**
     * 商户方的收款方账号
     */
    @TableField("merchant_payee_no")
    private String merchantPayeeNo;

    /**
     * 订单类型：01-消费;02-退货款;03-与平台方结算;04-理财;05-结息;06-借款;07-还款;99-其他
     */
    @TableField("order_type")
    private String orderType;

    /**
     * 状态：00-受理支付;99-失败;90-成功;98-处理超时;95-需人工处理
     */
    @TableField("order_status")
    private String orderStatus = STATUS_ACCEPT;

    /**
     * 支付额
     */
    @TableField("amt")
    private BigDecimal amt;

    /**
     * 接入商户号(目前只有orders)
     */
    @TableField("merchant_no")
    private String merchantNo;

    /**
     * 商户方的订单号
     */
    @TableField("merchant_order_no")
    private String merchantOrderNo;

    /**
     * 其他业务号
     */
    @TableField("bussiness_no")
    private String bussinessNo;

    /**
     * 事由
     */
    @TableField("incident")
    private String incident;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

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

    /**
     * 订单处理完成的（含成功和失败）
     */
    @TableField("finish_time")
    private LocalDateTime finishTime;
}
