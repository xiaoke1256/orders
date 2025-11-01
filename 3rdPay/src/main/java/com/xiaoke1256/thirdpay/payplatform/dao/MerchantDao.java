package com.xiaoke1256.thirdpay.payplatform.dao;

import com.xiaoke1256.thirdpay.payplatform.bo.Merchant;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商户表 服务类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-10-31
 */
public interface MerchantDao extends IService<Merchant> {

    Merchant getByMerchantNo(String merchantNo);

}
