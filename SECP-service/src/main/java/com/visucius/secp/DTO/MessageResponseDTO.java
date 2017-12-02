package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class MessageResponseDTO {
    @JsonProperty
    private String text;

    @JsonProperty
    private String author;

    @JsonProperty
    private long time;

    public MessageResponseDTO(String author, String text) {
        this.author = author;
        this.text = text;
        this.time = new Date().getTime();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
