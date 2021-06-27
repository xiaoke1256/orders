package com.xiaoke1256.orders.store.intra.cotroller;

import com.xiaoke1256.orders.store.intra.bo.UserInfo;
import com.xiaoke1256.orders.store.intra.encrypt.HMAC256;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource(name = "loginTokenGenerator")
    private HMAC256 loginTokenGenerator;

    @PostMapping("login")
    public Map<String,Object> login(String loginName, String password){
        //TODO 校验用户名密码
        //发放token
        Map<String,Object> retMap = new HashMap<>();
        String token = loginTokenGenerator.token(loginName);
        retMap.put("token",token);
        UserInfo user = new UserInfo();
        user.setLoginName(loginName);
        retMap.put("user",user);
        return retMap;
    }

    @GetMapping("verify")
    public Boolean verify(String token){
        return loginTokenGenerator.verify(token);
    }

    @GetMapping("loginName")
    public String getLoginNameFromToken(String token){
        return loginTokenGenerator.getContent(token);
    }
}
