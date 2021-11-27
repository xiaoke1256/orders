package com.xiaoke1256.orders.store.intra.login.controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/login/{wsToken}")
public class LoginSocket {

    private Session session;
    private String wsToken;

    @OnOpen
    public void onOpen(@PathParam("wsToken") String wsToken, Session session) throws IOException {
        session.getId();
    }

    @OnClose
    public void onClose() throws IOException {

    }

    @OnMessage
    public void onMessage(String message) throws IOException {

    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }


}
