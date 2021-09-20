package com.xiaoke1256.orders.store.intra.store.controller;

import com.xiaoke1256.orders.product.api.StoreMemberService;
import com.xiaoke1256.orders.product.dto.StoreMember;
import com.xiaoke1256.orders.store.intra.store.client.StoreMemberClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/storeMember")
public class StoreMemberController implements StoreMemberService {

    @Autowired
    private StoreMemberClient storeMemberClient;

    @Override
    @RequestMapping(value="/byAccountNo",method= RequestMethod.GET)
    public List<StoreMember> queryStoreMemberByAccountNo(String accountNo) {
        return storeMemberClient.queryStoreMemberByAccountNo(accountNo);
    }
}
