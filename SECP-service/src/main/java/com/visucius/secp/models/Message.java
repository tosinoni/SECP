package com.visucius.secp.models;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "message")
public class Message {

    @Id
    @Column(name = "message_id", unique = true, nullable = false)
    private int messageId;

    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

    @Column(name = "body")
    private String messageBody;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Message(Date dateTime, String messageBody, User user) {
        this.dateTime = new Date(dateTime.getTime());
        this.messageBody = messageBody;
        this.user = user;
    }

    public long getMessageId(){return messageId;}

    public void setMessageId(int messageId){this.messageId = messageId;}

    public Date getDateTime(){return new Date(dateTime.getTime());}

    public void setDateTime(Date dateTime){this.dateTime = new Date(dateTime.getTime());}

    public String getMessageBody(){return messageBody;}

    public void setMessageBody(String messageBody){this.messageBody = messageBody;}

    public User getUser(){return user;}

    public void setUser(User user){this.user = user;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message that = (Message) o;
        return messageId == that.messageId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + messageId;
        result = prime * result + user.hashCode();
        return result;
    }
}
