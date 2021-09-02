package com.xiaoke1256.orders.store.intra.common.utils;

import org.junit.Test;

public class HMAC256UtilTest {

    //token秘钥
    private static final String TOKEN_SECRET_1 = "XIAOKE1256JBFJH2021BQWE";

    private static final String TOKEN_SECRET_2 = "XIAOKE1256JBFJH2021BQWR";

    @Test
    public void token (){
        String token1 = HMAC256Util.token("AAAA","BBBBB",TOKEN_SECRET_1);
        System.out.println("token1:"+token1);
        String token2 = HMAC256Util.token("AAAA","BBBBB",TOKEN_SECRET_2);
        System.out.println("token2:"+token2);
    }

    @Test
    public void verify(){
        String token = HMAC256Util.token("AAAA","BBBBB",TOKEN_SECRET_1);
        System.out.println("token:"+token);
        boolean result = HMAC256Util.verify(token,TOKEN_SECRET_1);
        System.out.println("result:"+result);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Boolean isExpire = HMAC256Util.checkExpire(token);
        System.out.println("isExpire:"+isExpire);
    }

    @Test
    public void getUserName(){
        String token = HMAC256Util.token("AAAA","BBBBB",TOKEN_SECRET_1);
        System.out.println("token:"+token);
        String userName = HMAC256Util.getUserName(token,TOKEN_SECRET_1);
        System.out.println("userName:"+userName);
    }

}
