package com.visucius.secp.Chat;

import com.google.common.base.Optional;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.User;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class ChatSocketCreator implements WebSocketCreator {

    private static final Logger log = Logger.getLogger(ChatSocketCreator.class);

    private final UserDAO userRepository;
    private final IMessageHandler messageHandler;

    public ChatSocketCreator(UserDAO userRepository, IMessageHandler messageHandler)
    {
        this.userRepository = userRepository;
        this.messageHandler = messageHandler;
    }

    @Override
    @UnitOfWork
    public Object createWebSocket(
        ServletUpgradeRequest servletUpgradeRequest,
        ServletUpgradeResponse servletUpgradeResponse) {

        String[] paths = servletUpgradeRequest.getRequestPath().split("/");
        String userID = paths[paths.length -1];
        try
        {
            long id = Long.parseLong(userID);
            Optional<User> optionalUser = userRepository.getUserWithGroups(id);
            if(optionalUser.isPresent())
                return new ChatSocketListener(optionalUser.get(), this.messageHandler);
            log.error("Invalid user id was passed in");
        }
        catch (NumberFormatException exception)
        {
            log.error("Value passed in for user id is not a number", exception);
        }

        return null;
    }
}
