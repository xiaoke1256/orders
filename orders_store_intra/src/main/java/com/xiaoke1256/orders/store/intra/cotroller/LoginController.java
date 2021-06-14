package com.xiaoke1256.orders.store.intra.cotroller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class LoginController {
    @PostMapping("login")
    public String login(String loginName,String password){
        return UUID.randomUUID().toString();
    }
}
