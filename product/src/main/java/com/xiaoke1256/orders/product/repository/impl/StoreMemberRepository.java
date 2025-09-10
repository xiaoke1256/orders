package com.xiaoke1256.orders.product.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoke1256.orders.product.bo.StoreMember;
import com.xiaoke1256.orders.product.entity.ProductEntity;
import com.xiaoke1256.orders.product.entity.StoreMemberEntity;
import com.xiaoke1256.orders.product.mapper.StoreMemberMapper;
import com.xiaoke1256.orders.product.repository.IStoreMemberRepository;
import com.xiaoke1256.orders.common.mybatis.repository.impl.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 店员表 服务实现类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-10
 */
@Service
public class StoreMemberRepository extends CrudRepository<StoreMemberMapper, StoreMemberEntity> implements IStoreMemberRepository {

    @Override
    public List<StoreMemberEntity> selectByAccountNo(String accountNo) {
        LambdaQueryWrapper<StoreMemberEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreMemberEntity::getAccountNo,accountNo);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public StoreMemberEntity getDefaultStore(String accountNo) {
        LambdaQueryWrapper<StoreMemberEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreMemberEntity::getAccountNo,accountNo)
                .eq(StoreMemberEntity::getIsDefaultStore,"1");
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public void saveStoreMember(StoreMemberEntity storeMember) {
        baseMapper.insert(storeMember);
    }
}
