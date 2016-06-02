package com.example.syncdemo.persistence;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class SyncMessage extends RealmObject {
    @Required
    private String userName;

    @Required
    private String message;

    @Required
    private String userAvatarUrl;

    private long time;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
