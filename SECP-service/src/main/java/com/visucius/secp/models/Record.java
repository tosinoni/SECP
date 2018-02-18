package com.visucius.secp.models;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Records")
public class Record {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "editor_name", nullable = false)
    private String editorName;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ActionType actionType;

    @Lob
    @Column(name = "editor_action", nullable = false)
    private String editorAction;

    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public Record() {

    }

    public Record(String editorName, ActionType actionType, String editorAction, Date timestamp) {
        this.editorName = editorName;
        this.actionType = actionType;
        this.editorAction = editorAction;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getEditorAction() {
        return editorAction;
    }

    public void setEditorAction(String editorAction) {
        this.editorAction = editorAction;
    }

    public Date getTimestamp(){return new Date(timestamp.getTime());}

    public void setTimestamp(Date timestamp){this.timestamp = new Date(timestamp.getTime());}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Record)) return false;
        Record record = (Record) o;
        return id == record.id && editorName == record.editorName && editorAction.equals(record.editorAction)
            && actionType.equals(record.actionType) && timestamp.equals(record.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.editorName, this.editorAction, this.actionType, this.timestamp);
    }
}
