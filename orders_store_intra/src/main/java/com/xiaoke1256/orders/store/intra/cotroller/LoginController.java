package com.xiaoke1256.orders.store.intra.cotroller;

import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.orders.member.dto.Member;
import com.xiaoke1256.orders.store.intra.bo.UserInfo;
import com.xiaoke1256.orders.store.intra.client.MemberQueryClient;
import com.xiaoke1256.orders.store.intra.encrypt.HMAC256;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource(name = "loginTokenGenerator")
    private HMAC256 loginTokenGenerator;

    private MemberQueryClient memberQueryClient;

    @PostMapping("login")
    public Map<String,Object> login(String loginName, String password){
        // 校验用户名密码
        Member member = memberQueryClient.getMember(loginName);
        if(member == null){
            throw new BusinessException("用户名或密码错误！");
        }
        if(!password.equals(member.getPassword())){
            throw new BusinessException("用户名或密码错误！");
        }
        //发放token
        Map<String,Object> retMap = new HashMap<>();
        String token = loginTokenGenerator.token(loginName);
        retMap.put("token",token);
        UserInfo user = new UserInfo();
        user.setLoginName(loginName);
        user.setNickName(member.getNickName());
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
