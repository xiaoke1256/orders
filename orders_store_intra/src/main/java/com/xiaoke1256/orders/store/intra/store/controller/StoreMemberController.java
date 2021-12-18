package com.xiaoke1256.orders.store.intra.store.controller;

import com.xiaoke1256.orders.product.dto.StoreMember;
import com.xiaoke1256.orders.store.intra.common.utils.RequestUtil;
import com.xiaoke1256.orders.store.intra.store.client.StoreMemberClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/storeMember")
public class StoreMemberController {

    @Autowired
    private StoreMemberClient storeMemberClient;

    @RequestMapping(value="/byAccountNo",method= RequestMethod.GET)
    public List<StoreMember> queryStoreMemberByAccountNo(HttpServletRequest request) {
        String loginName = RequestUtil.getLoginName(request);
        return storeMemberClient.queryStoreMemberByAccountNo(loginName);
    }
}
