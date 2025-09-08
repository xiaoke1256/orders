package com.xiaoke1256.orders.product.repository.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.entity.ProductEntity;
import com.xiaoke1256.orders.product.mapper.ProductMapper;
import com.xiaoke1256.orders.product.repository.IProductRepository;
import com.xiaoke1256.orders.common.mybatis.repository.impl.CrudRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-08
 */
@Repository
public class ProductRepository extends CrudRepository<ProductMapper, ProductEntity> implements IProductRepository {

    @Override
    public List<ProductEntity> queryByCondition(ProductCondition condition) {
        LambdaQueryWrapper<ProductEntity> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(condition.getProductCode())){
            wrapper.like(ProductEntity::getProductCode,condition.getProductCode()+"%");
        }
        if (StringUtils.isNotBlank(condition.getProductName())){
            wrapper.like(ProductEntity::getProductName,"%"+condition.getProductName()+"%");
        }
        if(StringUtils.isNotBlank(condition.getStoreNo())){
            wrapper.eq(ProductEntity::getStoreNo,condition.getStoreNo());
        }
        if(condition.getStoreNos()!=null && condition.getStoreNos().length>0){
            wrapper.in(ProductEntity::getStoreNo, Arrays.asList(condition.getStoreNos()));
        }
        Long total = baseMapper.selectCount(wrapper);
        condition.setTotal(total.hashCode());
        return baseMapper.selectList(wrapper);
    }
}
