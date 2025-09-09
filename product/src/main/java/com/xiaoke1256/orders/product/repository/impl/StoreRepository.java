package com.xiaoke1256.orders.product.repository.impl;

import com.xiaoke1256.orders.product.entity.StoreEntity;
import com.xiaoke1256.orders.product.mapper.StoreMapper;
import com.xiaoke1256.orders.product.repository.IStoreRepository;
import com.xiaoke1256.orders.common.mybatis.repository.impl.CrudRepository;
import org.springframework.stereotype.Service;

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

}
