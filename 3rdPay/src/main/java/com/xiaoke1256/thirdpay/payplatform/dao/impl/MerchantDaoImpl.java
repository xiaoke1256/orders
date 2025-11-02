package com.xiaoke1256.thirdpay.payplatform.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoke1256.thirdpay.payplatform.bo.Merchant;
import com.xiaoke1256.thirdpay.payplatform.mapper.MerchantMapper;
import com.xiaoke1256.thirdpay.payplatform.dao.MerchantDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商户表 服务实现类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-10-31
 */
@Repository
public class MerchantDaoImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantDao {

    @Override
    public Merchant getByMerchantNo(String merchantNo) {
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Merchant::getMerchantNo,merchantNo);
        return this.baseMapper.selectOne(wrapper);
    }
}
