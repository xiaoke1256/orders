package com.xiaoke1256.orders.product.controller;


import com.xiaoke1256.orders.product.dto.StoreMember;
import com.xiaoke1256.orders.product.service.StoreMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/storeMember")
public class StoreMemberController implements com.xiaoke1256.orders.product.api.StoreMemberService {

    private StoreMemberService storeMemberService;

    @RequestMapping(value="/byAccountNo",method= RequestMethod.GET)
    public List<StoreMember> queryStoreMemberByAccountNo(String accountNo){
        List<com.xiaoke1256.orders.product.bo.StoreMember> bos = storeMemberService.selectByAccountNo(accountNo);
        List<StoreMember> dtos = new ArrayList<>();
        for(com.xiaoke1256.orders.product.bo.StoreMember bo:bos){
            StoreMember dto = new StoreMember();
            BeanUtils.copyProperties(bo, dto);
            dtos.add(dto);
        }
        return dtos;
    }
}
