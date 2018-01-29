package com.visucius.secp.Chat;

import com.visucius.secp.DTO.MessageDTO;
import com.visucius.secp.daos.GroupDAO;
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

    private ConcurrentHashMap<Long,Set<IMessageReceiver>> activeUsers;
    private final MessageDAO messageRepository;
    private final GroupDAO groupRepository;

    public ChatSocketHandler(MessageDAO messageRepository, GroupDAO groupRepository)
    {
        this.activeUsers = new ConcurrentHashMap<>();
        this.messageRepository = messageRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public void attachSession(IMessageReceiver messageReceiver) {

        long userID = messageReceiver.getUser().getId();
        if (activeUsers.containsKey(userID)) {
            activeUsers.get(userID).add(messageReceiver);
        } else {
            Set<IMessageReceiver> receivers = new HashSet<>();
            receivers.add(messageReceiver);
            activeUsers.put(userID, receivers);
        }

    }

    @Override
    public void detachSession(IMessageReceiver messageReceiver) {

        long userID = messageReceiver.getUser().getId();
        if (activeUsers.containsKey(userID)) {
            Set<IMessageReceiver> receivers = activeUsers.get(userID);
            receivers.remove(messageReceiver);
            if (receivers.isEmpty())
                activeUsers.remove(userID);

        }
    }

    @Override
    @UnitOfWork
    public void notifySession(MessageDTO message, IMessageReceiver receiver) {
        MessageDTO savedMessage = saveMessage(message, receiver);
        for(IMessageReceiver messageReceiver: getReceivers(message)) {
            messageReceiver.updateUser(savedMessage);
        }
    }

    private Set<IMessageReceiver> getReceivers(MessageDTO messageDTO)
    {
        Set<IMessageReceiver> receivers = new HashSet<>();
        if(messageDTO.getReason() == MessageDTO.MessageType.MESSAGE)
        {
            com.google.common.base.Optional<Group> groupOptional = groupRepository.find(messageDTO.getGroupId());
            if(groupOptional.isPresent())
            {
                Group group = groupOptional.get();
                group.getUsers().forEach(user -> {
                    long userId = user.getId();
                    if(this.activeUsers.containsKey(userId))
                        receivers.addAll(this.activeUsers.get(userId));
                });
            }
        }
        else {
            if(activeUsers.containsKey(messageDTO.getSenderId()))
                return activeUsers.get(messageDTO.getSenderId());
        }

        return receivers;
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
            messageDTO.getReason(),
            message.getTimestamp());

        if(user.getDisplayName() != null) {
            savedMessage.setSenderDisplayName(user.getDisplayName());
        }
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
