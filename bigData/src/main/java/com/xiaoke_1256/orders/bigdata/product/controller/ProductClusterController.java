package com.xiaoke_1256.orders.bigdata.product.controller;

import com.xiaoke_1256.orders.bigdata.product.service.ProductClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductClusterController {

    @Autowired
    private ProductClusterService productClusterService;

    @GetMapping("helloWord")
    public String helloWord(){
        return productClusterService.hello();
        //return "helloWord";
    }
}
