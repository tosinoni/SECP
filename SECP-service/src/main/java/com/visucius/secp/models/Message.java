package com.visucius.secp.models;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(name = "body", nullable = false)
    @Lob
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    public Message(String body, User user) {
        this.timestamp = new Date(timestamp.getTime());
        this.body = body;
        this.user = user;
    }

    public long getId(){return id;}

    public void setId(int id){this.id = id;}

    public Date getTimestamp(){return new Date(timestamp.getTime());}

    public void setTimestamp(Date timestamp){this.timestamp = new Date(timestamp.getTime());}

    public String getBody(){return body;}

    public void setBody(String body){this.body = body;}

    public User getUser(){return user;}

    public void setUser(User user){this.user = user;}

    public Group getGroup(){return this.group;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message that = (Message) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + user.hashCode();
        return result;
    }
}
