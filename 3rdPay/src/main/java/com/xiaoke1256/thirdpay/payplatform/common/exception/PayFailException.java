package com.xiaoke1256.thirdpay.payplatform.common.exception;

import com.xiaoke1256.orders.common.exception.BusinessException;

/**
 * 支付失败异常
 * 抛出本异常后,必须将订单状态改为失败
 * @author Administrator
 *
 */
public class PayFailException extends BusinessException {
    public PayFailException() {
        super();
    }

    public PayFailException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public PayFailException(String errorCode, String errorMsg, String showMsg) {
        super(errorCode, errorMsg, showMsg);
    }

    public PayFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PayFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayFailException(String showMsg) {
        super(showMsg);
    }

    public PayFailException(Throwable cause) {
        super(cause);
    }
}
