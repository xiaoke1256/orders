package com.xiaoke1256.orders.store.intra.utils;

import org.junit.Test;

public class HMAC256UtilTest {

    @Test
    public void token (){
        String token = HMAC256Util.token("AAAA","BBBBB");
        System.out.println("token:"+token);
    }

    @Test
    public void verify(){
        String token = HMAC256Util.token("AAAA","BBBBB");
        System.out.println("token:"+token);
        boolean result = HMAC256Util.verify(token);
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
        String token = HMAC256Util.token("AAAA","BBBBB");
        System.out.println("token:"+token);
        String userName = HMAC256Util.getUserName(token);
        System.out.println("userName:"+userName);
    }

}
