package com.xiaoke1256.thirdpay.domain.model.repository;

import com.xiaoke1256.thirdpay.domain.model.aggregate.HouseholdAcc;
import com.xiaoke1256.thirdpay.domain.model.valueobject.AccountNo;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 分户账仓储接口
 */
public interface HouseholdAccRepository {
    
    /**
     * 保存账户
     * @param account 账户聚合根
     * @return 保存后的账户聚合根
     */
    HouseholdAcc save(HouseholdAcc account);
    
    /**
     * 根据账号查询账户
     * @param accNo 账号
     * @return 账户聚合根，如果不存在则返回Optional.empty()
     */
    Optional<HouseholdAcc> findByAccNo(AccountNo accNo);
    
    /**
     * 根据账户ID查询账户
     * @param accId 账户ID
     * @return 账户聚合根，如果不存在则返回Optional.empty()
     */
    Optional<HouseholdAcc> findByAccId(Long accId);
    
    /**
     * 更新账户余额
     * @param accNo 账号
     * @param amount 变更金额（正值表示增加，负值表示减少）
     * @param version 版本号（用于乐观锁）
     * @return 更新成功返回true，否则返回false
     */
    boolean updateBalance(AccountNo accNo, BigDecimal amount, Long version);
    
    /**
     * 删除账户
     * @param accNo 账号
     * @return 删除成功返回true，否则返回false
     */
    boolean deleteByAccNo(AccountNo accNo);
}