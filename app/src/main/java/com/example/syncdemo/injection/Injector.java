package com.example.syncdemo.injection;

import android.content.Context;

import com.example.syncdemo.DemoConstants;
import com.example.syncdemo.persistence.PrefsHelper;
import com.example.syncdemo.persistence.SyncMessage;
import com.example.syncdemo.rest.MessageService;
import com.example.syncdemo.sync.SyncRequestHelper;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class Injector {

    public static Retrofit provideRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static MessageService provideMessageService(String baseUrl) {
        return provideRetrofit(baseUrl).create(MessageService.class);
    }

    public static MessageService provideMessageService() {
        return provideRetrofit(DemoConstants.BASE_URL).create(MessageService.class);
    }

    public static PrefsHelper providePrefsHelper(Context context) {
        return new PrefsHelper(context);
    }

    public static Realm provideRealm(Context context) {
        return Realm.getInstance(context);
    }

    public static SyncRequestHelper provideSyncRequestHelper() {
        return new SyncRequestHelper();
    }
}
