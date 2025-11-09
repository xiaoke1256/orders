package com.xiaoke1256.thirdpay.payplatform.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc;
import com.xiaoke1256.thirdpay.payplatform.dao.HouseholdAccDao;
import com.xiaoke1256.thirdpay.payplatform.mapper.HouseholdAccMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 分户账 服务实现类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-11-01
 */
@Repository
public class HouseholdAccDaoImpl extends ServiceImpl<HouseholdAccMapper, HouseholdAcc> implements HouseholdAccDao {
    public List<HouseholdAcc> findAll(){
        return new ArrayList<>();
    }

    @Override
    public HouseholdAcc findByAccNo(String accNo) {
        LambdaQueryWrapper<HouseholdAcc> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HouseholdAcc::getAccNo,accNo);
        return this.baseMapper.selectOne(wrapper);
    }

    @Override
    public HouseholdAcc lockByAccNo(String accNo) {
        LambdaQueryWrapper<HouseholdAcc> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HouseholdAcc::getAccNo,accNo).last("for update");
        return this.baseMapper.selectOne(wrapper);
    }

    @Override
    public void updateBalance(String accNo, BigDecimal newBalance) {
        LambdaUpdateWrapper<HouseholdAcc> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(HouseholdAcc::getAccNo,accNo);
        wrapper.set(HouseholdAcc::getBalance,newBalance);
        wrapper.set(HouseholdAcc::getUpdateTime, LocalDateTime.now());
        baseMapper.update(wrapper);
    }
}
