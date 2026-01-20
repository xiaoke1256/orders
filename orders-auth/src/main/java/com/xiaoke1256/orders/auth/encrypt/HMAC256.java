package com.xiaoke1256.orders.auth.encrypt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HMAC256 {

    //设置过期时间
    private long expireDate;

    {
        expireDate = 30 * 60 * 1000;
    }

    //token秘钥
    private String tokenSecret = "XIAOKE1256JBFJH2021BQWE";

    public HMAC256() {
    }

    public HMAC256(long expireDate, String tokenSecret) {
        this.expireDate = expireDate;
        this.tokenSecret = tokenSecret;
    }

    public String token (String content){

        String token = null;
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis()+expireDate);
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
            //设置头部信息
            Map<String,Object> header = new HashMap<>();
            header.put("typ","JWT");
            header.put("alg","HS256");
            //携带username，password信息，生成签名
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("content",content)
                    .withClaim("random", Math.abs(new Random().nextInt()))
                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .sign(algorithm);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
        return token;
    }

    public String token (Map<String,String> params){
        String token = null;
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis()+expireDate);
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
            //设置头部信息
            Map<String,Object> header = new HashMap<>();
            header.put("typ","JWT");
            header.put("alg","HS256");
            //携带username，password信息，生成签名
            JWTCreator.Builder builder = JWT.create()
                    .withHeader(header);
            for(Map.Entry<String,String> entry:params.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                builder.withClaim(key,value);
            }

            token = builder.withClaim("random", Math.abs(new Random().nextInt()))
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
    public boolean verify(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
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
    public String getContent(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("content").asString();
        } catch (TokenExpiredException e){
            throw e;
        } catch (JWTDecodeException e){
            e.printStackTrace();
            return null;
        }
    }

    public String getValue(String token,String key){
        try {
            Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim(key).asString();
        } catch (JWTDecodeException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * token是否过期
     * @param token
     */
    public boolean checkExpire(String token) {
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
