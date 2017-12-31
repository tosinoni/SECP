package com.visucius.secp.Chat;

import com.visucius.secp.models.Message;
import com.visucius.secp.models.User;

public interface IMessageReceiver {

    User getUser();
    boolean updateUser(Message message);
}
