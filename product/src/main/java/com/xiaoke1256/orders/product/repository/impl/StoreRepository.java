package com.xiaoke1256.orders.product.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.xiaoke1256.orders.product.bo.Store;
import com.xiaoke1256.orders.product.entity.StoreEntity;
import com.xiaoke1256.orders.product.mapper.StoreMapper;
import com.xiaoke1256.orders.product.repository.IStoreRepository;
import com.xiaoke1256.orders.common.mybatis.repository.impl.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商铺 服务实现类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-09
 */
@Service
public class StoreRepository extends CrudRepository<StoreMapper, StoreEntity> implements IStoreRepository {

    @Override
    public StoreEntity getByStoreNo(String storeNo) {
        LambdaQueryWrapper<StoreEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreEntity::getStoreNo,storeNo);
        return this.baseMapper.selectOne(wrapper);
    }

    @Override
    public void saveStore(StoreEntity store) {
        baseMapper.insert(store);
    }

    @Override
    public void updateStore(StoreEntity store) {
        baseMapper.updateById(store);
    }
}
