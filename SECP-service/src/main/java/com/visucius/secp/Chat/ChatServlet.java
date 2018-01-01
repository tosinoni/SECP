package com.visucius.secp.Chat;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;



public class ChatServlet extends WebSocketServlet {

    private static final long serialVersionUID = 0L;
    private transient ChatSocketCreator chatSocketCreator;

    public ChatServlet(ChatSocketCreator chatSocketCreator)
    {
        this.chatSocketCreator = chatSocketCreator;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.setCreator(this.chatSocketCreator);
    }
}
