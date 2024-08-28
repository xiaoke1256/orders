package com.xiaoke_1256.orders.bigdata.product.controller;

import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke_1256.orders.bigdata.product.dto.ProductCondition;
import com.xiaoke_1256.orders.bigdata.product.model.Product;
import com.xiaoke_1256.orders.bigdata.product.service.ProductClusterService;
import com.xiaoke_1256.orders.bigdata.product.service.ProductSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductClusterController {

    @Autowired
    private ProductClusterService productClusterService;

    @Autowired
    private ProductSearchService productSearchService;

    @GetMapping("helloWord")
    public String helloWord(){
        return productClusterService.hello();
    }

    @GetMapping("/searchProduct")
    public QueryResult<Product> searchProduct(ProductCondition productCondition){
        return productSearchService.searchByCondition(productCondition);
        //查出来后count订单量
    }
}
