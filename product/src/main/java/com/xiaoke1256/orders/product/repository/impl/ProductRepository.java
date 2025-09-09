package com.xiaoke1256.orders.product.repository.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.entity.ProductEntity;
import com.xiaoke1256.orders.product.entity.ProductTypeEntity;
import com.xiaoke1256.orders.product.entity.StoreEntity;
import com.xiaoke1256.orders.product.mapper.ProductMapper;
import com.xiaoke1256.orders.product.mapper.ProductTypeMapper;
import com.xiaoke1256.orders.product.mapper.StoreMapper;
import com.xiaoke1256.orders.product.repository.IProductRepository;
import com.xiaoke1256.orders.common.mybatis.repository.impl.CrudRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger logger = LoggerFactory.getLogger(ProductRepository.class);

    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Autowired
    private StoreMapper storeMapper;

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
        Page<ProductEntity> page = new Page<>(condition.getPageNo(), condition.getPageSize());
        Page<ProductEntity> productPage = baseMapper.selectPage(page, wrapper);
        productPage.getRecords().forEach((p)->{
            loadCascade(p);
            logger.info("p:"+ JSON.toJSONString(p));
        });
        return productPage.getRecords();
    }

    @Override
    public ProductEntity getProductByCode(String productCode) {
        LambdaQueryWrapper<ProductEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductEntity::getProductCode,productCode);
        ProductEntity entity = baseMapper.selectOne(wrapper);
        loadCascade(entity);
        return entity;
    }

    private void loadCascade(ProductEntity p){
        p.setProductTypes(productTypeMapper.getTypesByProductCode(p.getProductCode()));
        LambdaQueryWrapper<StoreEntity> storeWrapper = new LambdaQueryWrapper<>();
        storeWrapper.eq(StoreEntity::getStoreNo,p.getStoreNo());
        StoreEntity store = storeMapper.selectOne(storeWrapper,false);
        p.setStore(store);
    }
}
