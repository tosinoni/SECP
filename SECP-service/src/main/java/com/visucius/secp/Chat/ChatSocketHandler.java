package com.visucius.secp.Chat;

import com.visucius.secp.DTO.MessageDTO;
import com.visucius.secp.daos.MessageDAO;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.Message;
import com.visucius.secp.models.User;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.Date;
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
            if(activeGroups.containsKey(groupID)) {
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
            if(activeGroups.containsKey(groupID)) {
                Set<IMessageReceiver> receivers = activeGroups.get(groupID);
                receivers.remove(messageReceiver);
                if(receivers.isEmpty())
                    activeGroups.remove(groupID);
            }
        }
    }

    @Override
    @UnitOfWork
    public void notifySession(MessageDTO message, IMessageReceiver receiver) {
        long groupID = message.getGroupId();
        long senderID = receiver.getUser().getId();
        MessageDTO savedMessage = saveMessage(message, receiver);
        for(IMessageReceiver messageReceiver: activeGroups.get(groupID)) {
            long receiverID = messageReceiver.getUser().getId();
            if (receiverID != senderID)
                messageReceiver.updateUser(savedMessage);
        }
    }

    private MessageDTO saveMessage(MessageDTO messageDTO, IMessageReceiver receiver)
    {
        long groupID = messageDTO.getGroupId();
        long senderID = receiver.getUser().getId();
        User user = receiver.getUser();
        Group group = getGroup(groupID, user);
        Message message = new Message(messageDTO.getBody(),user,group);
        message.setTimestamp(new Date());
        Message createdMessage = messageRepository.save(message);
        MessageDTO savedMessage = new MessageDTO(
            createdMessage.getId(),
            createdMessage.getGroup().getId(),
            senderID,
            createdMessage.getBody(),
            messageDTO.getReason());
        savedMessage.setTimestamp(createdMessage.getTimestamp());
        return savedMessage;
    }

    private Group getGroup(long groupID, User user)
    {
        Optional<Group> optionalGroup = user.getGroups().
            stream().
            filter(group -> group.getId() == groupID).findFirst();
        if(optionalGroup.isPresent())
            return optionalGroup.get();

        throw new IllegalArgumentException(String.format("Group id %d is not valid.",groupID));
    }

}
