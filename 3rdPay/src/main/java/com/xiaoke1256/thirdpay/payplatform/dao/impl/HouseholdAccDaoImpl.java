package com.xiaoke1256.thirdpay.payplatform.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc;
import com.xiaoke1256.thirdpay.payplatform.dao.HouseholdAccDao;
import com.xiaoke1256.thirdpay.payplatform.mapper.HouseholdAccMapper;
import org.springframework.stereotype.Repository;

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
}
