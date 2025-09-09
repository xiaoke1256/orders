package com.xiaoke1256.orders.product.repository.impl;

import com.xiaoke1256.orders.product.entity.ProductTypeEntity;
import com.xiaoke1256.orders.product.mapper.ProductTypeMapper;
import com.xiaoke1256.orders.product.repository.IProductTypeRepository;
import com.xiaoke1256.orders.common.mybatis.repository.impl.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品分类 服务实现类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-08
 */
@Service
public class ProductTypeRepository extends CrudRepository<ProductTypeMapper, ProductTypeEntity> implements IProductTypeRepository {

}
