package com.xiaoke1256.orders.product.repository.impl;

import com.xiaoke1256.orders.product.entity.ProductParamEntity;
import com.xiaoke1256.orders.product.mapper.ProductParamMapper;
import com.xiaoke1256.orders.product.repository.IProductParamRepository;
import com.xiaoke1256.orders.common.mybatis.repository.impl.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品参数 服务实现类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-08
 */
@Service
public class ProductParamRepository extends CrudRepository<ProductParamMapper, ProductParamEntity> implements IProductParamRepository {

}
