package com.xiaoke1256.thirdpay.payplatform.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.xiaoke1256.thirdpay.payplatform.bo.Merchant;
import com.xiaoke1256.thirdpay.payplatform.dao.MerchantDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MerchantService {

    @Autowired
    private MerchantDao merchantDao;

    /**
     * 获取商户公钥
     * @param merchantNo
     * @return
     */
    @Transactional(readOnly = true)
    public String getPublicKey(String merchantNo) {
        //TODO 先从缓存中获取商户公钥

        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Merchant::getMerchantNo,merchantNo);
        Merchant merchant = merchantDao.getOne(wrapper);
        if(merchant != null)
            return merchant.getPublicKey();
        return null;
    }

    /**
     * 获取商户的收款账户
     * @param merchantNo
     * @return
     */
    @Transactional(readOnly = true)
    public String getAccNo(String merchantNo) {
        //TODO 先从缓存中获取商户公钥

        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Merchant::getMerchantNo,merchantNo);
        Merchant merchant = merchantDao.getOne(wrapper);
        if(merchant != null)
            return merchant.getDefaultAccNo();
        return null;
    }
}
