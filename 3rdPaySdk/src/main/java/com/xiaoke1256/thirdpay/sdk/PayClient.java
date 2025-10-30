package com.xiaoke1256.thirdpay.sdk;

import com.alibaba.fastjson.JSON;
import com.xiaoke1256.thirdpay.sdk.dto.OrderInfo;
import com.xiaoke1256.thirdpay.sdk.encryption.rsa.RSAKeyPairGenerator;
import com.xiaoke1256.thirdpay.sdk.encryption.rsa.RSAUtils;
import com.xiaoke1256.thirdpay.sdk.exception.BussinessException;
import com.xiaoke1256.thirdpay.sdk.vo.OrderFormInfo;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.UUID;

public class PayClient {
    /**
     * 生成订单表单
     * @param orderInfo
     * @param signKey 用商户的私钥对订单信息进行签名
     * @param encryptKey 用平台方公钥对订单信息进行加密
     * @return 订单表单字符串
     */
    public static String generateOrderFormString(OrderInfo orderInfo, PrivateKey signKey, PublicKey encryptKey) throws Exception {
        validateOrderInfo(orderInfo);
        orderInfo.setRandom(UUID.randomUUID().toString().replace("-", ""));
        orderInfo.setExpiredTime(System.currentTimeMillis() + 1000 * 60 * 5);//5分钟后过期
        OrderFormInfo orderFormInfo = new OrderFormInfo();
        orderFormInfo.setOrderInfo(orderInfo);
        orderFormInfo.setSign(Base64.getEncoder().encodeToString(RSAUtils.signData(JSON.toJSONString(orderInfo).getBytes(StandardCharsets.UTF_8), signKey)));
        orderFormInfo.setExpiredTime(orderInfo.getExpiredTime());
        return RSAUtils.encrypt(JSON.toJSONString(orderFormInfo),encryptKey);
    }

    /**
     * 生成订单表单
     * @param orderInfo
     * @param signKey 用商户的私钥对订单信息进行签名
     * @return 订单表单字符串
     */
    public static String generateOrderFormString(OrderInfo orderInfo, PrivateKey signKey) throws Exception {
        try( InputStream is = PayClient.class.getResourceAsStream("/3rdPay/cryptionkeys/public_key.pem")){
            return generateOrderFormString(orderInfo, signKey, RSAKeyPairGenerator.loadPublicKeyFromStream(is));
        }

    }

    /**
     * 生成订单表单
     * @param orderInfo
     * @param signKey 用商户的私钥对订单信息进行签名
     * @return 订单表单字符串
     */
    public static String generateOrderFormString(OrderInfo orderInfo, String signKey) throws Exception {
        try( InputStream is = PayClient.class.getResourceAsStream("/3rdPay/cryptionkeys/public_key.pem")){
            PrivateKey privateKey = RSAKeyPairGenerator.loadPrivateKey(signKey);
            return generateOrderFormString(orderInfo, privateKey, RSAKeyPairGenerator.loadPublicKeyFromStream(is));
        }

    }

    private static void validateOrderInfo(OrderInfo orderInfo) {
        if (StringUtils.isBlank(orderInfo.getPayerNo())){
            throw new BussinessException("error:payerNo can not be null.");
        }
        if (StringUtils.isBlank(orderInfo.getPayeeNo())){
            throw new BussinessException("error:payeeNo can not be null.");
        }
        if (StringUtils.isBlank(orderInfo.getOrderType())){
            throw new BussinessException("error:orderType can not be null.");
        }
        if (orderInfo.getAmt() == null){
            throw new BussinessException("error:amt can not be null.");
        }
        if (orderInfo.getAmt().compareTo(BigDecimal.ZERO)<=0){
            throw new BussinessException("error:amt must be greater than zero.");
        }
        if (StringUtils.isBlank(orderInfo.getMerchantNo())){
            throw new BussinessException("error:merchantNo can not be null.");
        }
        if (StringUtils.isBlank(orderInfo.getMerchantOrderNo())){
            throw new BussinessException("error:merchantOrderNo can not be null.");
        }
    }
}
