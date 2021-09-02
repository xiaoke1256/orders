package com.xiaoke1256.orders.store.intra.product.controller;

import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.SimpleProductQueryResultResp;
import com.xiaoke1256.orders.store.intra.product.client.ProductQueryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductQueryClient productQueryService;

    @RequestMapping(value="/product/search",method= RequestMethod.GET)
    public SimpleProductQueryResultResp searchProductByCondition(ProductCondition condition){
        return productQueryService.searchProductByCondition(condition);
    }
}
