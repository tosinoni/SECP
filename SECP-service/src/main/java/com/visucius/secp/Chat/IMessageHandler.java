package com.visucius.secp.Chat;

import com.visucius.secp.DTO.MessageDTO;

public interface IMessageHandler {

    void attachSession(IMessageReceiver messageReceiver);
    void detachSession(IMessageReceiver messageReceiver);
    void notifySession(MessageDTO message);
}
