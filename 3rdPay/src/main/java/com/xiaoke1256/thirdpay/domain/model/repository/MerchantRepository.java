package com.xiaoke1256.thirdpay.domain.model.repository;

import com.xiaoke1256.thirdpay.domain.model.aggregate.Merchant;

import java.util.List;
import java.util.Optional;

/**
 * 商户仓储接口
 */
public interface MerchantRepository {
    
    /**
     * 保存商户
     * @param merchant 商户聚合根
     * @return 保存后的商户聚合根
     */
    Merchant save(Merchant merchant);
    
    /**
     * 根据商户编号查询商户
     * @param merchantNo 商户编号
     * @return 商户聚合根，如果不存在则返回Optional.empty()
     */
    Optional<Merchant> findByMerchantNo(String merchantNo);
    
    /**
     * 根据商户ID查询商户
     * @param merchantId 商户ID
     * @return 商户聚合根，如果不存在则返回Optional.empty()
     */
    Optional<Merchant> findByMerchantId(Long merchantId);
    
    /**
     * 查询所有商户
     * @return 商户列表
     */
    List<Merchant> findAll();
    
    /**
     * 根据状态查询商户
     * @param status 状态值
     * @return 商户列表
     */
    List<Merchant> findByStatus(Integer status);
    
    /**
     * 更新商户信息
     * @param merchant 商户聚合根
     * @return 更新后的商户聚合根
     */
    boolean update(Merchant merchant);
    
    /**
     * 删除商户
     * @param merchantNo 商户编号
     * @return 删除成功返回true，否则返回false
     */
    boolean deleteByMerchantNo(String merchantNo);
}