package com.xiaoke1256.thirdpay.payplatform.dao;

import com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 第三方支付记录表 服务类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-11-06
 */
public interface ThirdPayOrderDao extends IService<ThirdPayOrder> {

    void updateStatus(String orderNo, String statusSuccess, Timestamp updateTime, Timestamp finishTime);

    ThirdPayOrder findByOrderNo(String orderNo);

    ThirdPayOrder lockByOrderNo(String orderNo);

    List<String> findOrderNosByLimitTime(String statusAccept, Date expiredTime);
}
