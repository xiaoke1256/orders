package com.xiaoke1256.orders.store.intra.product.controller;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.SimpleProductQueryResultResp;
import com.xiaoke1256.orders.product.dto.StoreMember;
import com.xiaoke1256.orders.store.intra.common.utils.RequestUtil;
import com.xiaoke1256.orders.store.intra.product.client.ProductClient;
import com.xiaoke1256.orders.store.intra.product.client.ProductQueryClient;
import com.xiaoke1256.orders.store.intra.store.client.StoreMemberClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductQueryClient productQueryService;

    @Autowired
    private ProductClient productService;

    @Autowired
    private StoreMemberClient storeMemberClient;

    @RequestMapping(method= RequestMethod.GET)
    public SimpleProductQueryResultResp searchProductByCondition(HttpServletRequest request, ProductCondition condition){
        String loginName = RequestUtil.getLoginName(request);
        List<StoreMember> storeMembers = storeMemberClient.queryStoreMemberByAccountNo(loginName);
        List<String> storeNos = storeMembers.stream().map(StoreMember::getStoreNo).collect(Collectors.toList());
        condition.setStoreNos(storeNos.toArray(new String[storeNos.size()]));
        SimpleProductQueryResultResp result = productQueryService.searchProductByCondition(condition);
        if(RespCode.SUCCESS.getCode().equals(result.getCode())){
            return result;
        }else if(RespCode.BUSSNESS_ERROR.getCode().equals(result.getCode())){
            throw new BusinessException(result.getCode(),result.getMsg());
        }else{
            throw new RuntimeException(result.getCode()+":"+result.getMsg());
        }
    }

    @PostMapping("{productCode}/switchShfs")
    public Boolean switchShfs(@PathVariable("productCode") String productCode, String onOrOff){
        productService.switchShelves(productCode,onOrOff);
        return true;
    }
}
