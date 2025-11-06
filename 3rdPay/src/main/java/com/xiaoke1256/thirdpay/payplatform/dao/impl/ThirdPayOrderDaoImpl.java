package com.xiaoke1256.thirdpay.payplatform.dao.impl;

import com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder;
import com.xiaoke1256.thirdpay.payplatform.mapper.ThirdPayOrderMapper;
import com.xiaoke1256.thirdpay.payplatform.dao.ThirdPayOrderDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 第三方支付记录表 服务实现类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-11-06
 */
@Service
public class ThirdPayOrderDaoImpl extends ServiceImpl<ThirdPayOrderMapper, ThirdPayOrder> implements ThirdPayOrderDao {

    @Override
    public void updateStatus(String orderNo, String statusSuccess, Timestamp now, Timestamp now1) {
        //do nothing
    }

    @Override
    public ThirdPayOrder findByOrderNo(String orderNo) {
        return null;
    }

    @Override
    public List<String> findOrderNosByLimitTime(String statusAccept, Date expiredTime) {
        return Collections.emptyList();
    }
}
