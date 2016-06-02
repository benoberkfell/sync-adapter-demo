package com.example.syncdemo;

import android.app.Application;

import timber.log.Timber;

public class SyncDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}
