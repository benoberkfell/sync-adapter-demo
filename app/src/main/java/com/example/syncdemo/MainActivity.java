package com.example.syncdemo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.syncdemo.injection.Injector;
import com.example.syncdemo.persistence.SyncMessage;
import com.example.syncdemo.sync.SyncRequestHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {


    /* This is the account user name, and what
     * what will show up in the accounts screen for the account we
     * wind up creating. */
    public static final String DEMO_USER_NAME = "user@example.com";

    Observable<List<SyncMessage>> messagesObservable;

    @Bind(R.id.message_recycler_view)
    RecyclerView messagesRecyclerView;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    MessagesAdapter messagesAdapter;
    RecyclerView.LayoutManager layoutManager;

    Realm realm;

    Subscription messagesSubscription;

    SyncRequestHelper syncRequestHelper;

    AccountManager accountManager;

    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        
        accountManager = AccountManager.get(this);

        layoutManager = new LinearLayoutManager(this);
        messagesRecyclerView.setLayoutManager(layoutManager);

        realm = Injector.provideRealm(this);
        syncRequestHelper = Injector.provideSyncRequestHelper();

        account = getAccount();
        messagesAdapter = new MessagesAdapter();
        messagesRecyclerView.setAdapter(messagesAdapter);

        if (account == null) {
            account = createAccount();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fireSync();
            }
        });

        requestPeriodicSync();
    }

    @Override
    public void onResume() {
        super.onResume();

        messagesSubscription = realm.where(SyncMessage.class)
                .findAllSortedAsync("time", Sort.DESCENDING).asObservable()
                .subscribe(new Action1<List<SyncMessage>>() {
                    @Override
                    public void call(List<SyncMessage> messages) {
                        messagesAdapter.setMessageResults(messages);
                        messagesAdapter.notifyDataSetChanged();
                    }
                });

    }

    @Override
    public void onPause() {
        messagesSubscription.unsubscribe();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    public void requestPeriodicSync() {
        syncRequestHelper.requestPeriodicSyncWithMinuteFrequency(account, DemoConstants.MESSAGES_CONTENT_AUTHORITY, 30);
    }

    private void fireSync() {
        syncRequestHelper.requestImmediateSync(account, DemoConstants.MESSAGES_CONTENT_AUTHORITY);
    }

    private Account createAccount() {
        Account account = new Account(DEMO_USER_NAME, DemoConstants.ACCOUNT_TYPE);
        accountManager.addAccountExplicitly(account, null, null);
        return account;
    }


    protected Account getAccount() {
        // lazily only support one account, since we only insert that one anyway
        Account[] accounts = accountManager.getAccountsByType(DemoConstants.ACCOUNT_TYPE);
        if (accounts.length > 0) {
            return accounts[0];
        }
        return null;
    }
}
