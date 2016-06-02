package com.example.syncdemo;

import com.example.syncdemo.injection.Injector;
import com.example.syncdemo.persistence.PrefsHelper;
import com.example.syncdemo.persistence.SyncMessage;
import com.example.syncdemo.rest.MessageService;
import com.example.syncdemo.sync.MessageSyncResult;
import com.example.syncdemo.sync.MessagesSyncer;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import io.realm.Realm;
import mockit.Mocked;
import mockit.Verifications;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

import static org.junit.Assert.assertEquals;

public class MessagesSyncerTest {

    @Mocked
    Realm mockedRealm;

    @Mocked
    PrefsHelper mockedPrefsHelper;

    @Test
    public void testFailureSetsIoException() {
        Retrofit retrofit = Injector.provideRetrofit(DemoConstants.BASE_URL);

        NetworkBehavior networkBehavior = NetworkBehavior.create();
        networkBehavior.setFailurePercent(100);
        networkBehavior.setFailureException(new IOException());

        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(networkBehavior)
                .build();

        MessageService service = mockRetrofit.create(MessageService.class).returningResponse(null);

        MessagesSyncer syncer = new MessagesSyncer(mockedRealm,
                mockedPrefsHelper,
                service);

        MessageSyncResult result = syncer.performSync();

        assertEquals(result.numIoExceptions, 1);
    }

    @Test
    public void testSuccessfulSync() throws Exception {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(Files.toString(new File("src/test/res/successful_sync.json"), Charsets.UTF_8)));

        MessageService service = Injector.provideMessageService(mockWebServer.url("/").toString());

        MessagesSyncer syncer = new MessagesSyncer(mockedRealm,
                mockedPrefsHelper,
                service);

        MessageSyncResult result = syncer.performSync();

        assertEquals(result.numIoExceptions, 0);

        new Verifications() {{
            mockedRealm.beginTransaction();
            mockedRealm.copyToRealm((SyncMessage) any); times = 5;
            mockedRealm.close();
            mockedPrefsHelper.setRecentSyncTime(1454645280);
        }};
    }
}
