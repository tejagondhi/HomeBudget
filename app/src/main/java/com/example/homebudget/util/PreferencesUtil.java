package com.example.homebudget.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {
    private final Context context;

    public PreferencesUtil(Context context) {
        this.context = context;
    }

    public void setPreference(String name, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(name, value);
        editor.apply();
    }

    public String getPreference(String name) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(name, null);
    }

    public void logout() {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
}
