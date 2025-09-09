package com.xiaoke1256.orders.product.mapper;

import com.xiaoke1256.orders.product.bo.ProductType;
import com.xiaoke1256.orders.product.entity.ProductTypeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 商品分类 Mapper 接口
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-08
 */
@Mapper
public interface ProductTypeMapper extends BaseMapper<ProductTypeEntity> {
    public List<ProductTypeEntity> getTypesByProductCode(String productCode);
}
