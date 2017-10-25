package com.visucius.secp.Chat;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;



public class ChatServerServlet extends WebSocketServlet {

    public ChatServerServlet()
    {
    }


    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(Endpoint.class);
    }
}
