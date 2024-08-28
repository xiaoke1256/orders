package com.xiaoke1256.orders.core.controller;

import com.xiaoke1256.orders.core.service.OStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 库存表
 */
@RestController
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private OStorageService oStorageService;
    /**
     * 补货
     */
    @PostMapping("/incStorage")
    public Boolean incStorage(@RequestParam("productCode") String productCode, @RequestParam(value = "optionCode",required = false)String optionCode, @RequestParam("incNum")int incNum) {
        oStorageService.incStorage(productCode,optionCode,incNum);
        return Boolean.TRUE;
    }


    @GetMapping("/getStorageNum")
    public long getStorageNumByProductCode(@RequestParam("productCode")String productCode){
        return oStorageService.getStorageNumByProductCode(productCode);
    }
}
