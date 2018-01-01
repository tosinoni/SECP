package com.visucius.secp.Chat;

import com.visucius.secp.DTO.MessageDTO;
import com.visucius.secp.daos.MessageDAO;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.Message;
import com.visucius.secp.models.User;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatSocketHandler implements IMessageHandler {

    private ConcurrentHashMap<Long,Set<IMessageReceiver>> activeGroups;
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
            long groupID = group.getId();
            if(activeGroups.contains(groupID)) {
                activeGroups.get(groupID).add(messageReceiver);
            }
            else
            {
                Set<IMessageReceiver> receivers = new HashSet<>();
                receivers.add(messageReceiver);
                activeGroups.put(groupID,receivers);
            }
        }
    }

    @Override
    public void detachSession(IMessageReceiver messageReceiver) {
        Set<Group> groups = messageReceiver.getUser().getGroups();
        for (Group group : groups) {
            long groupID = group.getId();
            if(activeGroups.contains(groupID)) {
                Set<IMessageReceiver> receivers = activeGroups.get(groupID);
                receivers.remove(groupID);
                if(receivers.isEmpty())
                    activeGroups.remove(groupID);
            }
        }
    }

    @Override
    @UnitOfWork
    public void notifySession(MessageDTO message) {
        long groupID = message.groupId;
        long senderID = message.userId;
        MessageDTO savedMessage = saveMessage(message);
        for(IMessageReceiver messageReceiver: activeGroups.get(groupID)) {
            long receiverID = messageReceiver.getUser().getId();
            if (receiverID != senderID)
                messageReceiver.updateUser(savedMessage);
        }
    }

    private MessageDTO saveMessage(MessageDTO messageDTO)
    {
        long groupID = messageDTO.groupId;
        User user = getUser(messageDTO.userId, groupID);
        Group group = getGroup(groupID, user);
        Message message = new Message(messageDTO.body,user,group, messageDTO.timestamp);
        Message createdMessage = messageRepository.save(message);
        messageDTO.messageId = createdMessage.getId();
        return messageDTO;
    }

    private User getUser(long userID, long groupID) {
        if (activeGroups.contains(groupID)) {
            Set<IMessageReceiver> receivers = activeGroups.get(groupID);
            Optional<IMessageReceiver> optionalReceiver = receivers.
                stream().
                filter((receiver) -> receiver.getUser().getId() == userID).findFirst();
            if (optionalReceiver.isPresent())
                return optionalReceiver.get().getUser();
            else
                throw new IllegalArgumentException("User id is not valid.");
        }

        throw new IllegalArgumentException("Group id is not valid.");
    }

    private Group getGroup(long groupID, User user)
    {
        Optional<Group> optionalGroup = user.getGroups().
            stream().
            filter(group -> group.getId() == groupID).findFirst();
        if(optionalGroup.isPresent())
            return optionalGroup.get();

        throw new IllegalArgumentException("Group id is not valid.");
    }

}
