package com.visucius.secp.Entity;

import org.joda.time.DateTime;
import java.util.HashSet;
import java.util.Set;

public class Room {

    private long Id;
    private String name;
    private User owner;
    private DateTime creationTime;
    private int permissionLevel;
    private Set<User> subscribers;
    private Privacy privacy;

    public Room()
    {
        this.subscribers = new HashSet<>();
    }

    public boolean addSubscriber(User user)
    {
        if(user.getPermissionLevel() < this.permissionLevel)
            return false;

        return subscribers.add(user);

    }

    public boolean removeSubscriber(User user)
    {
        return subscribers.remove(user);
    }

}
