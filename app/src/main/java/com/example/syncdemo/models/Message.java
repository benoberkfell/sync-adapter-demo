package com.example.syncdemo.models;

import com.google.gson.annotations.Expose;

public class Message {

    @Expose
    private long time;

    @Expose
    private String user_name;

    @Expose
    private String avatar;

    @Expose
    private String message;

    public long getTime() {
        return time;
    }

    public String getUserName() {
        return user_name;
    }

    public String getAvatarUrl() {
        return avatar;
    }

    public String getMessage() {
        return message;
    }
}
