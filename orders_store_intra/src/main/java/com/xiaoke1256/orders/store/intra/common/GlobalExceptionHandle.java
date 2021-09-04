package com.xiaoke1256.orders.store.intra.common;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandle {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RespMsg handleException(Exception e){
        if (e instanceof BusinessException) {
            BusinessException err = (BusinessException)e;
            return new RespMsg(RespCode.BUSSNESS_ERROR,err.getShowMsg());
        }else{
            return new RespMsg(RespCode.OTHER_ERROR);
        }
    }
}
