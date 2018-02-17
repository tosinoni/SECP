package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.visucius.secp.models.ActionType;
import com.visucius.secp.models.Record;

import java.util.Date;

public class RecordsDTO {

    @JsonProperty
    private long id;

    @JsonProperty
    private String editorName;

    @JsonProperty
    private ActionType actionType;

    @JsonProperty
    private String editorAction;

    @JsonProperty
    private Date timestamp;

    public RecordsDTO() {

    }

    public RecordsDTO(Record record) {
        this.id = record.getId();
        this.editorName = record.getEditorName();
        this.actionType = record.getActionType();
        this.editorAction = record.getEditorAction();
        this.timestamp = record.getTimestamp();
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

    public Date getTimestamp() {
        return new Date(timestamp.getTime());
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = new Date(timestamp.getTime());
    }
}
