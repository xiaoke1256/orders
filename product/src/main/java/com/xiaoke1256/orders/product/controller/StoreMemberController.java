package com.xiaoke1256.orders.product.controller;


import com.xiaoke1256.orders.product.dto.Store;
import com.xiaoke1256.orders.product.dto.StoreMember;
import com.xiaoke1256.orders.product.service.StoreMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/storeMember")
public class StoreMemberController implements com.xiaoke1256.orders.product.api.StoreMemberService {

    @Autowired
    private StoreMemberService storeMemberService;

    @RequestMapping(value="/byAccountNo",method= RequestMethod.GET)
    public List<StoreMember> queryStoreMemberByAccountNo(@RequestParam String accountNo){
        List<com.xiaoke1256.orders.product.domain.StoreMember> domains = storeMemberService.selectByAccountNo(accountNo);
        List<StoreMember> dtos = new ArrayList<>();
        for(com.xiaoke1256.orders.product.domain.StoreMember domain:domains){
            StoreMember dto = new StoreMember();
            BeanUtils.copyProperties(domain, dto);
            dto.setStore(new Store());
            BeanUtils.copyProperties(domain.getStore(),dto.getStore());
            dtos.add(dto);
        }
        return dtos;
    }
}
