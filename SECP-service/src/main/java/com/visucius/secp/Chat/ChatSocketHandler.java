package com.visucius.secp.Chat;

import com.visucius.secp.daos.MessageDAO;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.Message;
import com.visucius.secp.models.User;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatSocketHandler implements IMessageHandler {

    private ConcurrentHashMap<Group,Set<IMessageReceiver>> activeGroups;
    private final MessageDAO messageRepository;

    public ChatSocketHandler(MessageDAO messageRepository)
    {
        this.activeGroups = new ConcurrentHashMap<>();
        this.messageRepository = messageRepository;
    }

    @Override
    public void attachSession(IMessageReceiver messageReceiver) {
        Set<Group> groups = messageReceiver.getUser().getGroups();
        for(Group group: groups)
        {
            if(activeGroups.contains(group)) {
                activeGroups.get(group).add(messageReceiver);
            }
            else
            {
                Set<IMessageReceiver> receivers = new HashSet<>();
                receivers.add(messageReceiver);
                activeGroups.put(group,receivers);
            }
        }
    }

    @Override
    public void detachSession(IMessageReceiver messageReceiver) {
        Set<Group> groups = messageReceiver.getUser().getGroups();
        for (Group group : groups) {
            if(activeGroups.contains(group)) {
                Set<IMessageReceiver> receivers = activeGroups.get(group);
                receivers.remove(group);
                if(receivers.isEmpty())
                    activeGroups.remove(messageReceiver);
            }
        }
    }

    @Override
    @UnitOfWork
    public void notifySession(Message message) {

        messageRepository.save(message);
        Group group = message.getGroup();
        User sender = message.getUser();
        for(IMessageReceiver messageReceiver: activeGroups.get(group))
        {
            User receiver = messageReceiver.getUser();
            if(!receiver.equals(sender))
                messageReceiver.updateUser(message);
        }
    }
}
