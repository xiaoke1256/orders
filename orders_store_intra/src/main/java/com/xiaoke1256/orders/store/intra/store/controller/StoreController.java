package com.xiaoke1256.orders.store.intra.store.controller;

import com.xiaoke1256.orders.product.dto.Store;
import com.xiaoke1256.orders.store.intra.common.encrypt.HMAC256;
import com.xiaoke1256.orders.store.intra.common.utils.RequestUtil;
import com.xiaoke1256.orders.store.intra.store.client.StoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/store")
public class StoreController {
    @Autowired
    private StoreClient storeService;

    @Resource(name = "loginTokenGenerator")
    private HMAC256 loginTokenGenerator;

    @RequestMapping(method= RequestMethod.POST)
    public Boolean saveStore(HttpServletRequest request, Store store){
        String loginName = RequestUtil.getLoginName(request);
        storeService.createStore(store,loginName);
        return true;
    }
}
