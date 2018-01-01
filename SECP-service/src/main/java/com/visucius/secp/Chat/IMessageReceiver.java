package com.visucius.secp.Chat;

import com.visucius.secp.DTO.MessageDTO;
import com.visucius.secp.models.User;

public interface IMessageReceiver {

    User getUser();
    boolean updateUser(MessageDTO message);
}
