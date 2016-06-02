package com.example.syncdemo.persistence;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefsHelper {

    private final SharedPreferences prefs;

    public PrefsHelper(Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setRecentSyncTime(long syncTime) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(PreferenceNames.RECENT_SYNC_KEY, syncTime);
        editor.apply();
    }

    public long getRecentSyncTime() {
        return prefs.getLong(PreferenceNames.RECENT_SYNC_KEY, 0);
    }
}
