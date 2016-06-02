package com.example.syncdemo.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MessageListResponse {

    @Expose
    private List<Message> messages;

    @Expose
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
