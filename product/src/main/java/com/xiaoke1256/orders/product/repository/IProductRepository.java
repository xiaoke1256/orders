package com.xiaoke1256.orders.product.repository;

import com.xiaoke1256.orders.product.bo.Product;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.entity.ProductEntity;
import com.xiaoke1256.orders.common.mybatis.repository.IRepository;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-08
 */
public interface IProductRepository extends IRepository<ProductEntity> {
    List<ProductEntity> queryByCondition(ProductCondition condition);
}
