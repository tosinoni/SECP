package com.visucius.secp.Chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.visucius.secp.DTO.MessageDTO;
import com.visucius.secp.models.User;
import com.visucius.secp.util.JsonUtil;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;
import java.util.Objects;


@WebSocket
public class ChatSocketListener implements WebSocketListener, IMessageReceiver {

    private static final Logger log = Logger.getLogger(ChatSocketListener.class);

    private Session outbound;
    private final User user;
    private final IMessageHandler messageHandler;

    public ChatSocketListener(User user, IMessageHandler messageHandler)
    {
        this.user = user;
        this.messageHandler = messageHandler;
    }

    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {

    }

    @Override
    public void onWebSocketText(String value) {

        log.info("Message was sent from" + user.getUsername());
        try
        {
            MessageDTO message = JsonUtil.convertStringToJson(value,MessageDTO.class);
            this.messageHandler.notifySession(message, this);
        }
        catch (IOException ioException)
        {
            log.error("Message received is not in correct format",ioException);
        }
        catch (IllegalArgumentException argumentException)
        {
            log.error(argumentException.getMessage(),argumentException);
        }
    }

    @Override
    public void onWebSocketClose(int code, String message) {

        log.info("WebSocket was closed, code: " + code + ", message: " + message);
        this.messageHandler.detachSession(this);
    }

    @Override
    public void onWebSocketConnect(Session session) {

        log.info("New session was added : " + session.getLocalAddress().getHostName());
        this.outbound = session;
        this.messageHandler.attachSession(this);
    }

    @Override
    public void onWebSocketError(Throwable throwable) {

        log.error("WebSocket error has occurred: ", throwable);
        this.messageHandler.detachSession(this);
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public boolean updateUser(MessageDTO message) {

        try {
            String messageString = JsonUtil.convertToJsonString(message);
            this.outbound.getRemote().sendString(messageString);
        }
        catch (JsonProcessingException parseException)
        {
            log.error("Error converting message to json", parseException);
            return false;
        }
        catch (IOException ioException)
        {
            log.error("Error sending message to "+ user.getUsername(), ioException);
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.user,this.outbound);
    }
}
