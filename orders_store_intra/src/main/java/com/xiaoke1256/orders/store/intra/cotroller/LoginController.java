package com.xiaoke1256.orders.store.intra.cotroller;

import com.xiaoke1256.orders.store.intra.encrypt.HMAC256;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
@RequestMapping("/login")
public class LoginController {
//    @Resource(name = "loginTokenGenerator")
//    private HMAC256 loginTokenGenerator;
//
//    @PostMapping("login")
//    public String login(String loginName,String password){
//        //TODO 校验用户名密码
//        //发放token
//        return loginTokenGenerator.token(loginName);
//    }
//
//    @GetMapping("verify")
//    public Boolean verify(String token){
//        return loginTokenGenerator.verify(token);
//    }
//
//    @GetMapping("loginName")
//    public String getLoginNameFromToken(String token){
//        return loginTokenGenerator.getContent(token);
//    }
}
