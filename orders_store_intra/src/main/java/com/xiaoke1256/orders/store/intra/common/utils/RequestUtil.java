package com.xiaoke1256.orders.store.intra.common.utils;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.xiaoke1256.orders.auth.encrypt.HMAC256;
import com.xiaoke1256.orders.common.exception.InvalidAuthorizationException;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

    private static HMAC256 loginTokenGenerator;

    public static String getLoginName(HttpServletRequest request){
        if(loginTokenGenerator==null){
            loginTokenGenerator = (HMAC256)ApplicationContextUtil.getBean("loginTokenGenerator");
        }
        String token = request.getHeader("Authorization");
        if(token==null){
            throw new InvalidAuthorizationException("未登录，请先登录");
        }
        try {
            String loginName = loginTokenGenerator.getContent(token);
            return loginName;
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            throw new InvalidAuthorizationException("登录已过期，请重新登录");
        }
    }
}
