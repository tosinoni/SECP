package com.visucius.secp.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "message")
@NamedQueries(
    {
        @NamedQuery(
            name = "com.visucius.secp.models.Message.findMessageWithGroupID",
            query = "select m from Message m join m.group g where g.id = :groupID order by m.timestamp DESC"
        )
    }
)
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private long id;

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

    public Message()
    {

    }

    public Message(String body, User user, Group group) {
        this.body = body;
        this.user = user;
        this.group = group;
    }

    public long getId(){return id;}

    public void setId(long id){this.id = id;}

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
        return Objects.hash(this.id,this.body,this.timestamp,this.group,this.user);
    }
}
