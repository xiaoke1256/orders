package com.xiaoke1256.thirdpay.payplatform.mq;

import com.alibaba.fastjson.JSON;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.thirdpay.payplatform.bo.Merchant;
import com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder;
import com.xiaoke1256.thirdpay.payplatform.service.MerchantService;
import com.xiaoke1256.thirdpay.payplatform.service.ThirdPayService;
import com.xiaoke1256.thirdpay.sdk.dto.NotifyInfo;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付后续处理的消费者
 */
@Service
@RocketMQMessageListener(topic = "3rdPay_post_payment", consumerGroup = "3rdPayPostPaymentGroup")
public class PostPaymentConsumer implements RocketMQListener<String> {

    private static final Logger logger = LoggerFactory.getLogger(PostPaymentConsumer.class);

    @Autowired
    private ThirdPayService ThirdPayService;

    @Autowired
    private MerchantService merchantService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void onMessage(String orderJson) {
        String notifyUrl = null;
        ThirdPayOrder order = null;
        try {
            order = JSON.parseObject(orderJson, ThirdPayOrder.class);
            logger.info("开始处理订单：{}",order.getOrderNo());
            //获取商户配置中的回调地址。
            Merchant merchant = merchantService.getByMerchantNo(order.getMerchantNo());
            notifyUrl = merchant.getNotifyUrl();

            ThirdPayService.doPostPayment(order.getOrderNo());
            sentNotify(order, notifyUrl,null);
        } catch (AppException e) {
            logger.error(e.getMessage(), e);
            if (notifyUrl!=null && order!=null) {
                sentNotify(order, notifyUrl, e);
            }else{
                //TODO 记录异常信息
            }
        }
    }

    private void sentNotify(ThirdPayOrder order, String notifyUrl,AppException appException) {
        try {
            //拼上参数
            NotifyInfo notifyInfo = new NotifyInfo();
            notifyInfo.setOrderNo(order.getOrderNo());
            notifyInfo.setStatus(order.getOrderStatus());
            notifyInfo.setMerchantOrderNo(order.getMerchantOrderNo());
            notifyInfo.setBussinessNo(order.getBussinessNo());
            if(appException==null) {
                notifyInfo.setResultCode(RespMsg.SUCCESS.getCode());
                notifyInfo.setMsg(RespMsg.SUCCESS.getMsg());
                restTemplate.postForObject(notifyUrl, notifyInfo, String.class);
            }else{
                notifyInfo.setResultCode(appException.getErrorCode());
                notifyInfo.setMsg(appException.getErrorMsg());
                restTemplate.postForObject(notifyUrl, notifyInfo, String.class);
            }
        } catch (Exception e) {
            //TODO 回调失败记录异常信息。
            logger.error(e.getMessage(),e);
        }
    }


}
