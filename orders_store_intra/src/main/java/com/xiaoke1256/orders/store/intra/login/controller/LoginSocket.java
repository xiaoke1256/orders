package com.xiaoke1256.orders.store.intra.login.controller;


import com.xiaoke1256.orders.store.intra.common.utils.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 用websoket方式登录
 */
public class LoginSocket extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(LoginSocket.class);

    /**
     * 目前的链接数
     */
    private static final Map<String, WebSocketSession> sessions = new HashMap<>();

    /**
     * 每个连接对应一个密钥对(TODO 这个以后要放到redits中)
     */
    private static final Map<String,KeyPair > keyPairs = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        LOG.info("ws connected.  {}",(String)session.getAttributes().get("sessionId"));
        sessions.put((String)session.getAttributes().get("sessionId"),session);
        setKeyPair((String)session.getAttributes().get("sessionId"));
        LOG.info("put success.");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 应该不会调用此方法，因为本ws不会接受客户端的消息。
        throw new RuntimeException("无效方法");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        if (session.isOpen()) {
            session.close();
        }
        //logger.info("连接出错");
        sessions.remove((String)session.getAttributes().get("sessionId"));
        keyPairs.remove((String)session.getAttributes().get("sessionId"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessions.remove((String)session.getAttributes().get("sessionId"));
        keyPairs.remove((String)session.getAttributes().get("sessionId"));
        LOG.info("WebSocket closed.");
    }

    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages();
    }

    public boolean sendMessageToUser(String sessionId, TextMessage message) {
        if (sessions.get(sessionId) == null)
            return false;
        WebSocketSession session = sessions.get(sessionId);
        //logger.info("sendMessage：{} ,msg：{}", session, message.getPayload());
        if (!session.isOpen()) {
            //logger.info("客户端:{},已断开连接，发送消息失败", mchNo);
            return false;
        }
        try {
            //
            session.sendMessage(message);
        } catch (IOException e) {
            //logger.info("sendMessageToUser method error：{}", e);
            return false;
        }
        return true;
    }

    /**
     * 检查会话是否存在
     * @param sessionId
     * @return
     */
    public boolean hasSession(String sessionId){
        return sessions.get(sessionId) != null;
    }

    public String decode(String encodeMessage,String sessionId) {
        try {
            return RSAUtil.decrypt(encodeMessage,keyPairs.get(sessionId).getPrivate());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getPublicKey(String sessionId){
        KeyPair keyPair = setKeyPair(sessionId);
        PublicKey publicKey = keyPair.getPublic();
        return publicKey.getEncoded();
    }

    private KeyPair setKeyPair(String sessionId) {
        KeyPair keyPair = keyPairs.get(sessionId);
        if(keyPair==null){
            //有可能还没有连接成功就要给密钥了
            try {
                keyPair = RSAUtil.genKeyPair();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            keyPairs.put(sessionId,keyPair);
        }
        return keyPair;
    }
}
