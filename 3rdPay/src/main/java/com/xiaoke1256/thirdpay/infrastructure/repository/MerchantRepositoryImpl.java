package com.xiaoke1256.thirdpay.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoke1256.thirdpay.domain.model.aggregate.Merchant;
import com.xiaoke1256.thirdpay.domain.model.repository.MerchantRepository;
//import com.xiaoke1256.thirdpay.payplatform.bo.Merchant;
import com.xiaoke1256.thirdpay.payplatform.mapper.MerchantMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 商户仓储实现
 */
@Repository
public class MerchantRepositoryImpl implements MerchantRepository {

    @Autowired
    private MerchantMapper merchantMapper;

    @Override
    public Merchant save(Merchant merchant) {
        com.xiaoke1256.thirdpay.payplatform.bo.Merchant bo = convertToBo(merchant);
        if (merchant.getMerchantId() == null) {
            merchantMapper.insert(bo);
        } else {
            merchantMapper.updateById(bo);
        }
        merchant.setMerchantId(bo.getMerchantId());
        return merchant;
    }

    @Override
    public Optional<Merchant> findByMerchantNo(String merchantNo) {
        LambdaQueryWrapper<com.xiaoke1256.thirdpay.payplatform.bo.Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(com.xiaoke1256.thirdpay.payplatform.bo.Merchant::getMerchantNo, merchantNo);
        com.xiaoke1256.thirdpay.payplatform.bo.Merchant bo = merchantMapper.selectOne(wrapper);
        return bo != null ? Optional.of(convertToAggregate(bo)) : Optional.empty();
    }

    @Override
    public Optional<Merchant> findByMerchantId(Long merchantId) {
        com.xiaoke1256.thirdpay.payplatform.bo.Merchant bo = merchantMapper.selectById(merchantId);
        return bo != null ? Optional.of(convertToAggregate(bo)) : Optional.empty();
    }

    @Override
    public List<Merchant> findAll() {
        LambdaQueryWrapper<com.xiaoke1256.thirdpay.payplatform.bo.Merchant> wrapper = new LambdaQueryWrapper<>();
        List<com.xiaoke1256.thirdpay.payplatform.bo.Merchant> listOfBo = merchantMapper.selectObjs(wrapper);
        return listOfBo.stream().map(this::convertToAggregate).collect(Collectors.toList());
    }

    @Override
    public List<Merchant> findByStatus(Integer status) {
//        LambdaQueryWrapper<com.xiaoke1256.thirdpay.payplatform.bo.Merchant> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(com.xiaoke1256.thirdpay.payplatform.bo.Merchant::getMerchantStatus, status);
//        List<com.xiaoke1256.thirdpay.payplatform.bo.Merchant> boList = merchantMapper.selectList(wrapper);
//        return boList.stream().map(this::convertToAggregate).collect(Collectors.toList());
        return new ArrayList<>();//暂时不设商户状态
    }

    @Override
    public boolean update(Merchant merchant) {
        com.xiaoke1256.thirdpay.payplatform.bo.Merchant bo = convertToBo(merchant);
        return merchantMapper.updateById(bo) > 0;
    }

    @Override
    public boolean deleteByMerchantNo(String merchantNo) {
        LambdaQueryWrapper<com.xiaoke1256.thirdpay.payplatform.bo.Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(com.xiaoke1256.thirdpay.payplatform.bo.Merchant::getMerchantNo, merchantNo);
        return merchantMapper.delete(wrapper) > 0;
    }

    // 转换方法
    private com.xiaoke1256.thirdpay.payplatform.bo.Merchant convertToBo(Merchant aggregate) {
        com.xiaoke1256.thirdpay.payplatform.bo.Merchant bo = new com.xiaoke1256.thirdpay.payplatform.bo.Merchant();
        BeanUtils.copyProperties(aggregate, bo);
        return bo;
    }

    private Merchant convertToAggregate(com.xiaoke1256.thirdpay.payplatform.bo.Merchant bo) {
        return new Merchant.Builder()
                .setMerchantId(bo.getMerchantId())
                .setMerchantNo(bo.getMerchantNo())
                .setMerchantName(bo.getMerchantName())
//                .setMerchantKey(bo.getMerchantKey())
//                .setMerchantPubKey(bo.getMerchantPubKey())
                .setNotifyUrl(bo.getNotifyUrl())
                .setSuccessReturnUrl(bo.getSuccessReturnUrl())
                .setFailReturnUrl(bo.getFailReturnUrl())
//                .setMerchantStatus(bo.getMerchantStatus())
//                .setRemark(bo.getRemark())
                .setInsertTime(bo.getInsertTime())
                .setUpdateTime(bo.getUpdateTime())
                .build();
    }
}