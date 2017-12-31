package com.visucius.secp.Chat;

import com.visucius.secp.models.Group;
import com.visucius.secp.models.Message;
import com.visucius.secp.models.User;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatSocketHandler implements IMessageHandler {

    private ConcurrentHashMap<Group,Set<IMessageReceiver>> activeGroups;

    public ChatSocketHandler()
    {
        this.activeGroups = new ConcurrentHashMap<>();
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
    public void notifySession(Message message) {
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
