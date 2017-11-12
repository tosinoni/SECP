package com.visucius.secp.models;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


enum conversationType {
    //PRIVATE = 2 users
    //GROUP = 2+ users
    PRIVATE, GROUP
}

@Entity
@Table(name = "conversation")
public class Conversation {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @ManyToOne
    private Set<Message> messages = new HashSet<>();

    @ManyToMany
    private Set<User> users = new HashSet<>();

    @Column(name = "conversation_type", unique = true)
    private conversationType CT;

    public long getId() {return id;}

    public Set<User> getUsers(){return this.users;}

    public Set<Message> getMessages(){return this.messages;}

    public Conversation(Set<User> users){
        this.users = users;

        this.CT = conversationType.PRIVATE;

        if (users.size() > 2){
            this.CT = conversationType.GROUP;
        }

    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Conversation)) return false;
        Conversation conversation = (Conversation) o;
        return id == conversation.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
