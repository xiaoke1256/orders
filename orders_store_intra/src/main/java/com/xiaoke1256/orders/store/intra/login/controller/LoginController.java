package com.xiaoke1256.orders.store.intra.login.controller;

import com.xiaoke1256.orders.auth.encrypt.HMAC256;
import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.orders.common.exception.InvalidAuthorizationException;
import com.xiaoke1256.orders.common.security.MD5Util;
import com.xiaoke1256.orders.member.dto.Member;
import com.xiaoke1256.orders.store.intra.login.bo.UserInfo;
import com.xiaoke1256.orders.store.intra.login.client.MemberQueryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource(name = "loginTokenGenerator")
    private HMAC256 loginTokenGenerator;

    @Resource(name = "refreshTokenGenerator")
    private HMAC256 refreshTokenGenerator;

    @Autowired
    private MemberQueryClient memberQueryClient;

    @Autowired
    private LoginSocket loginSocket;

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
        Map<String,String> tokenParam = new HashMap<>();
        tokenParam.put("loginName",loginName);
        tokenParam.put("LOGTK_ENCODE", MD5Util.getMD5(token).substring(0,8));
        String refreshToken = refreshTokenGenerator.token(tokenParam);
        retMap.put("token",token);
        retMap.put("refreshToken",refreshToken);
        UserInfo user = new UserInfo();
        user.setLoginName(loginName);
        user.setNickName(member.getNickName());
        retMap.put("user",user);
        return retMap;
    }

    /**
     * 二维码登录
     * @param encodeMessage 加密信息，包括登录名和校验码
     * @param randomCode 随机校验码
     * @param sessionId
     * @return 登录是否成功。
     */
    @PostMapping("loginWith2dCode")
    public Boolean loginWith2dCode(String encodeMessage,String randomCode,String sessionId){
        //TODO 以后要法消息的办法来解决
        if(!loginSocket.hasSession(sessionId)){
            return false;
        }
        String decodeMessage = loginSocket.decode(encodeMessage, sessionId);
        if(!decodeMessage.endsWith(randomCode)){
            return false;
        }
        String loginName = decodeMessage.substring(0,decodeMessage.length()-randomCode.length());
        //检查一下数据库中是否存在他
        Member member = memberQueryClient.getMember(loginName);
        if(member==null){
            return false;
        }
        //校验正确发放Token
        Map<String,Object> retMap = new HashMap<>();
        String token = loginTokenGenerator.token(loginName);
        Map<String,String> tokenParam = new HashMap<>();
        tokenParam.put("loginName",loginName);
        tokenParam.put("LOGTK_ENCODE", MD5Util.getMD5(token).substring(0,8));
        String refreshToken = refreshTokenGenerator.token(tokenParam);
        retMap.put("token",token);
        retMap.put("refreshToken",refreshToken);
        UserInfo user = new UserInfo();
        user.setLoginName(loginName);
        user.setNickName(member.getNickName());
        retMap.put("user",user);
        //JSON.toJSONString(retMap);
        loginSocket.sendMessageToUser(sessionId,new TextMessage(retMap.toString()));

        return true;
    }

    @PostMapping("sessionId")
    public String getSessionId(HttpServletRequest request){
        return request.getSession(true).getId();
    }

    /**
     * token 失效后用 refreshToken 来获取新的token
     * @param request
     * @param refreshToken
     * @return
     */
    @PostMapping("refresh")
    public Map<String,Object> refresh(HttpServletRequest request, String refreshToken){
        if(!refreshTokenGenerator.verify(refreshToken)){
            throw new InvalidAuthorizationException("校验不通过。");
        }
        String token = request.getHeader("Authorization");
        String loginName = refreshTokenGenerator.getValue(refreshToken,"loginName");

        if(!MD5Util.getMD5(token).substring(0,8).equals(refreshTokenGenerator.getValue(refreshToken,"LOGTK_ENCODE"))){
            throw new InvalidAuthorizationException("校验不通过。");
        }

        //重新发放token对
        Map<String,Object> retMap = new HashMap<>();
        String newToken = loginTokenGenerator.token(loginName);
        Map<String,String> tokenParam = new HashMap<>();
        tokenParam.put("loginName",loginName);
        tokenParam.put("LOGTK_ENCODE", MD5Util.getMD5(newToken).substring(0,8));
        String newRefreshToken = refreshTokenGenerator.token(tokenParam);
        retMap.put("token",newToken);
        retMap.put("refreshToken",newRefreshToken);

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
