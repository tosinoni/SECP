package com.visucius.secp.Chat;

import com.visucius.secp.models.Message;

public interface IMessageHandler {

    void attachSession(IMessageReceiver messageReceiver);
    void detachSession(IMessageReceiver messageReceiver);
    void notifySession(Message message);
}
