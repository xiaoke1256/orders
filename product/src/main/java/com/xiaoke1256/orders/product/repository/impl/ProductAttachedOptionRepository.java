package com.xiaoke1256.orders.product.repository.impl;

import com.xiaoke1256.orders.product.entity.ProductAttachedOptionEntity;
import com.xiaoke1256.orders.product.mapper.ProductAttachedOptionMapper;
import com.xiaoke1256.orders.product.repository.IProductAttachedOptionRepository;
import com.xiaoke1256.orders.common.mybatis.repository.impl.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品附加选项 服务实现类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-08
 */
@Service
public class ProductAttachedOptionRepository extends CrudRepository<ProductAttachedOptionMapper, ProductAttachedOptionEntity> implements IProductAttachedOptionRepository {

}
