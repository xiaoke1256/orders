package com.xiaoke1256.orders.store.intra.login.controller;


import com.xiaoke1256.orders.store.intra.common.utils.RSAUtil;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

/**
 * 用websoket方式登录
 */
public class LoginSocket extends TextWebSocketHandler {

    /**
     * 目前的链接数
     */
    private static final Map<String, WebSocketSession> sessions = new HashMap<>();

    /**
     * 每个连接对应一个密钥对
     */
    private static final Map<String,KeyPair > keyPairs = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.put((String)session.getAttributes().get("sessionId"),session);
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
}
