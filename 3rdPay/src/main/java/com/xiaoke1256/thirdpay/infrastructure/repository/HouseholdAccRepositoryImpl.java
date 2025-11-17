package com.xiaoke1256.thirdpay.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xiaoke1256.thirdpay.domain.model.aggregate.HouseholdAcc;
import com.xiaoke1256.thirdpay.domain.model.repository.HouseholdAccRepository;
import com.xiaoke1256.thirdpay.domain.model.valueobject.AccountNo;
// HouseholdAccBo import removed as we're now using HouseholdAcc directly
import com.xiaoke1256.thirdpay.payplatform.mapper.HouseholdAccMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 分户账仓储实现
 */
@Repository
public class HouseholdAccRepositoryImpl implements HouseholdAccRepository {

    @Autowired
    private HouseholdAccMapper accountMapper;

    @Override
    public HouseholdAcc save(HouseholdAcc account) {
        com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc bo = convertToBo(account);
        if (account.getAccId() == null) {
            accountMapper.insert(bo);
        } else {
            accountMapper.updateById(bo);
        }
        account.setAccId(bo.getAccId());
        return account;
    }

    @Override
    public Optional<HouseholdAcc> findByAccNo(AccountNo accNo) {
        LambdaQueryWrapper< com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq( com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc::getAccNo, accNo.getValue());
        com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc bo = accountMapper.selectOne(wrapper);
        return bo != null ? Optional.of(convertToAggregate(bo)) : Optional.empty();
    }

    @Override
    public Optional<HouseholdAcc> findByAccId(Long accId) {
        com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc bo = accountMapper.selectById(accId);
        return bo != null ? Optional.of(convertToAggregate(bo)) : Optional.empty();
    }

    @Override
    public boolean updateBalance(AccountNo accNo, BigDecimal amount, Long version) {
        LambdaUpdateWrapper<com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc::getAccNo, accNo.getValue())
               .set(com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc::getUpdateTime, LocalDateTime.now());
        
        // 执行余额更新，这里使用MyBatis-Plus的字段更新功能
        wrapper.setSql("balance = balance + " + amount);
        
        return accountMapper.update(null, wrapper) > 0;
    }

    @Override
    public boolean deleteByAccNo(AccountNo accNo) {
        LambdaQueryWrapper<com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc::getAccNo, accNo.getValue());
        return accountMapper.delete(wrapper) > 0;
    }

    // 转换方法
    private com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc convertToBo(HouseholdAcc aggregate) {
        com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc bo = new com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc();
        BeanUtils.copyProperties(aggregate, bo);
        bo.setAccNo(aggregate.getAccNo().getValue());
        bo.setBalance(aggregate.getBalance().getAmount());
        return bo;
    }

    private HouseholdAcc convertToAggregate( com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc bo) {
        return new HouseholdAcc.Builder()
                .setAccId(bo.getAccId())
                .setAccNo(AccountNo.of(bo.getAccNo()))
                .setAccName(bo.getAccName())
                .setBalance(com.xiaoke1256.thirdpay.domain.model.valueobject.Money.of(bo.getBalance()))
                .setInsertTime(bo.getInsertTime())
                .setUpdateTime(bo.getUpdateTime())
                .build();
    }
}