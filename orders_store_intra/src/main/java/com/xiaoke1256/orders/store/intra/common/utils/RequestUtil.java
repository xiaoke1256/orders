package com.xiaoke1256.orders.store.intra.common.utils;

import com.xiaoke1256.orders.store.intra.common.encrypt.HMAC256;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

    private static HMAC256 loginTokenGenerator;

    public static String getLoginName(HttpServletRequest request){
        if(loginTokenGenerator==null){
            loginTokenGenerator = (HMAC256)ApplicationContextUtil.getBean("loginTokenGenerator");
        }
        String token = request.getHeader("Authorization");
        String loginName = loginTokenGenerator.getContent(token);
        return loginName;
    }
}
