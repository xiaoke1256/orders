package com.xiaoke1256.thirdpay.payplatform.dao;

import com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 分户账 服务类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-11-01
 */
public interface HouseholdAccDao extends IService<HouseholdAcc> {
    List<HouseholdAcc> findAll();

    HouseholdAcc findByAccNo(String accNo);

    HouseholdAcc lockByAccNo(String accNo);

    void updateBalance(String accNo, BigDecimal newBalance);
}
