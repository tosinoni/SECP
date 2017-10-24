package com.visucius.secp.Chat;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;


@WebSocket
public class Endpoint {

    private static final Logger log = Logger.getLogger(Endpoint.class);

    @OnWebSocketConnect
    public void onOpen(Session session)
    {
        log.info("New session was added : " + session.getLocalAddress().getHostName());
    }

    @OnWebSocketMessage
    public void  onMessage(Session session, String content)
    {
        log.info("Message was received " + content);
    }

    @OnWebSocketClose
    public void onClose(int code, String message)
    {
        log.info("WebSocket was closed, code: " + code + ", message: " + message);
    }

    @OnWebSocketError
    public void onError(Throwable throwable)
    {
        log.error("WebSocket error has occured: ", throwable);
    }
}
