package com.xiaoke1256.thirdpay.interfaces.rest;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.thirdpay.application.dto.PaymentRequest;
import com.xiaoke1256.thirdpay.application.dto.PaymentResponse;
import com.xiaoke1256.thirdpay.application.service.ThirdPayApplicationService;
import com.xiaoke1256.thirdpay.domain.model.aggregate.ThirdPayOrder;
import com.xiaoke1256.thirdpay.domain.model.valueobject.OrderNo;
import com.xiaoke1256.thirdpay.interfaces.rest.dto.PaymentCallbackRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    
    @Autowired
    private ThirdPayApplicationService thirdPayApplicationService;
    
    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            logger.info("create payment order, merchant no: {}", paymentRequest.getMerchantNo());
            PaymentResponse response = thirdPayApplicationService.createPayment(paymentRequest);
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            logger.error("create payment order failed: {}", e.getMessage());
            PaymentResponse response = new PaymentResponse();
            response.setErrorCode(e.getErrorCode());
            response.setErrorMsg(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("create payment order system error: {}", e.getMessage(), e);
            PaymentResponse response = new PaymentResponse();
            response.setErrorCode("SYSTEM_ERROR");
            response.setErrorMsg("system internal error");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @GetMapping("/orders/{orderNo}")
    public ResponseEntity<PaymentResponse> queryOrder(@PathVariable("orderNo") String orderNo) {
        try {
            logger.info("query order request, order no: {}", orderNo);
            ThirdPayOrder order = thirdPayApplicationService.queryOrder(OrderNo.of(orderNo));
            
            PaymentResponse response = new PaymentResponse();
            response.setOrderNo(order.getOrderNo().getValue());
            response.setAmount(order.getAmt().getValue());
            response.setStatus(order.getOrderStatus().getCode());
            
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            logger.error("query order failed: {}", e.getMessage());
            PaymentResponse response = new PaymentResponse();
            response.setErrorCode(e.getErrorCode());
            response.setErrorMsg(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("query order system error: {}", e.getMessage(), e);
            PaymentResponse response = new PaymentResponse();
            response.setErrorCode("SYSTEM_ERROR");
            response.setErrorMsg("system internal error");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @PostMapping("/callback")
    public RespMsg handleCallback(@RequestBody PaymentCallbackRequest callbackRequest) {
        try {
            logger.info("receive payment callback, order no: {}, status: {}", callbackRequest.getOrderNo(), callbackRequest.getStatus());
            
            thirdPayApplicationService.handlePaymentCallback(
                    OrderNo.of(callbackRequest.getOrderNo()),
                    callbackRequest.getStatus(),
                    callbackRequest.getTransactionNo()
            );
            
            return RespMsg.SUCCESS;
        } catch (Exception e) {
            logger.error("handle payment callback failed: {}", e.getMessage(), e);
            return new RespMsg(RespCode.OTHER_ERROR, "handle callback failed");
        }
    }
    
    @PostMapping("/close-expired")
    public RespMsg closeExpiredOrders() {
        try {
            logger.info("execute close expired orders task");
            thirdPayApplicationService.closeExpiredOrders();
            return RespMsg.SUCCESS;
        } catch (Exception e) {
            logger.error("close expired orders failed: {}", e.getMessage(), e);
            return new RespMsg(RespCode.OTHER_ERROR, "close expired orders failed");
        }
    }
}