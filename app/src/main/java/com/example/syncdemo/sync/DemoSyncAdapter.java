package com.example.syncdemo.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.example.syncdemo.injection.Injector;
import com.example.syncdemo.persistence.PrefsHelper;
import com.example.syncdemo.rest.MessageService;

public class DemoSyncAdapter extends AbstractThreadedSyncAdapter {

    MessageService messageService;
    PrefsHelper prefsHelper;

    public DemoSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        injectMembers(context);
    }

    public DemoSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        injectMembers(context);
    }

    private void injectMembers(Context context) {
        messageService = Injector.provideMessageService();
        prefsHelper = Injector.providePrefsHelper(getContext());
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {


        MessagesSyncer syncer = new MessagesSyncer(Injector.provideRealm(getContext()),
                prefsHelper, messageService);
        MessageSyncResult result = syncer.performSync();

        // Apply the potential soft errors from our result to the sync
        syncResult.stats.numIoExceptions += result.numIoExceptions;

        // report the number of values inserted
        // if we had an error, but made progress, it will immediately
        // retry
        syncResult.stats.numInserts += result.numInserts;
    }
}
