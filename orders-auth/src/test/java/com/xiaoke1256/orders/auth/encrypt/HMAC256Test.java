package com.xiaoke1256.orders.auth.encrypt;

public class HMAC256Test {
    public static void main(String[] args) {
        HMAC256 hmac256 = new HMAC256(6*60*60*1000,"XIAOKE1256JGRTY2021BQWE");
        String token = hmac256.token("username=xiaoke1256&role=1,2,3");
        System.out.println("token:"+token);
    }
}
