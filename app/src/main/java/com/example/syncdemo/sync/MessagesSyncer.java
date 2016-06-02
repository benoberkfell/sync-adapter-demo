package com.example.syncdemo.sync;

import com.example.syncdemo.models.Message;
import com.example.syncdemo.models.MessageListResponse;
import com.example.syncdemo.persistence.PrefsHelper;
import com.example.syncdemo.persistence.SyncMessage;
import com.example.syncdemo.rest.MessageService;

import java.io.IOException;

import io.realm.Realm;

public class MessagesSyncer {

    private final Realm realm;
    private final PrefsHelper prefsHelper;
    private final MessageService messageService;

    public MessagesSyncer(Realm realm,
                          PrefsHelper prefsHelper,
                          MessageService messageService) {

        this.realm = realm;
        this.prefsHelper = prefsHelper;
        this.messageService = messageService;

    }

    public MessageSyncResult performSync() {
        long recentSyncTime = prefsHelper.getRecentSyncTime();

        MessageSyncResult syncResult = new MessageSyncResult();

        // We need to get our Realm on the thread that onPerformSync happens in.
        try {
            int valuesAdded = 0;

            MessageListResponse response = messageService.recentMessages(recentSyncTime).execute().body();
            realm.beginTransaction();
            for (Message m : response.getMessages()) {
                SyncMessage savedMessage = new SyncMessage();
                savedMessage.setMessage(m.getMessage());
                savedMessage.setUserName(m.getUserName());
                savedMessage.setUserAvatarUrl(m.getAvatarUrl());
                savedMessage.setTime(m.getTime());
                realm.copyToRealm(savedMessage);
                valuesAdded++;
            }
            realm.commitTransaction();

            prefsHelper.setRecentSyncTime(response.getTimestamp());
            syncResult.numInserts = valuesAdded;
        } catch (IOException e) {
            syncResult.numIoExceptions++;
        } finally {
            realm.close();
        }

        return syncResult;
    }
}
