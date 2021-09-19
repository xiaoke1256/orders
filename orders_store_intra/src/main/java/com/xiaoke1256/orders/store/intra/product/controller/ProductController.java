package com.xiaoke1256.orders.store.intra.product.controller;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.SimpleProductQueryResultResp;
import com.xiaoke1256.orders.store.intra.product.client.ProductQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductQueryClient productQueryService;

    @RequestMapping(method= RequestMethod.GET)
    public SimpleProductQueryResultResp searchProductByCondition(ProductCondition condition){
        LOG.info("pageNo:{}",condition.getPageNo());
        SimpleProductQueryResultResp result = productQueryService.searchProductByCondition(condition);
        if(RespCode.SUCCESS.getCode().equals(result.getCode())){
            return result;
        }else if(RespCode.BUSSNESS_ERROR.getCode().equals(result.getCode())){
            throw new BusinessException(result.getCode(),result.getMsg());
        }else{
            throw new RuntimeException(result.getCode()+":"+result.getMsg());
        }
    }
}
