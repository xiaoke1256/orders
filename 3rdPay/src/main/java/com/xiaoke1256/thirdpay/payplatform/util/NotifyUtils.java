package com.xiaoke1256.thirdpay.payplatform.util;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder;
import com.xiaoke1256.thirdpay.sdk.dto.NotifyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class NotifyUtils {

    private static final Logger logger = LoggerFactory.getLogger(NotifyUtils.class);

    private final static RestTemplate restTemplate = new RestTemplate();
    public  static void sentNotify(ThirdPayOrder order, String notifyUrl, AppException appException) {
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
