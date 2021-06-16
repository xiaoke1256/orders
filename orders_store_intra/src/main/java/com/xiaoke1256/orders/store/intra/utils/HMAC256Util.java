package com.xiaoke1256.orders.store.intra.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
//import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HMAC256Util {

    //设置过期时间
    private static final long EXPIRE_DATE=30*60*1000;
    //token秘钥
    private static final String TOKEN_SECRET = "XIAOKE1256JBFJH2021BQWE";

    public static String token (String username,String password,String secret){

        String token = "";
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis()+EXPIRE_DATE);
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(secret);
            //设置头部信息
            Map<String,Object> header = new HashMap<>();
            header.put("typ","JWT");
            header.put("alg","HS256");
            //携带username，password信息，生成签名
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("username",username)
                    .withClaim("password",password)
                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .sign(algorithm);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
        return token;
    }

    /**
     * 检验token是否正确
     * @param **token**
     * @return
     */
    public static boolean verify(String token,String secret){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            @SuppressWarnings("unused")
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }


    /**
     * 从token中获取username信息
     * @param **token**
     * @return
     */
    public static String getUserName(String token,String secret){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            @SuppressWarnings("unused")
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * token是否过期
     * @param token
     */
    public static Boolean checkExpire(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            //获取token过期时间
            Date expiretime = jwt.getExpiresAt();
            String etStr = String.valueOf(expiretime.getTime());
            //获取系统当前时间
            String nowTime = String.valueOf(System.currentTimeMillis());
            //如果系统当前时间超过token过期时间返回false
            if(nowTime.compareTo(etStr)>0){
                return false;
            }
            return true;
        } catch (JWTDecodeException e){
            e.printStackTrace();
        }
        return true;
    }

}
