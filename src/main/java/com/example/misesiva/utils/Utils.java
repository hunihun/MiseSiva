package com.example.misesiva.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

    public static void setPreferences(Context context, String alias, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(alias, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPreferences(Context context, String alias, String key) {
        SharedPreferences pref = context.getSharedPreferences(alias, context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static void removePreferences(Context context, String alias, String key) {
        SharedPreferences pref = context.getSharedPreferences(alias, context.MODE_PRIVATE);
        if (!pref.getString(key, "").isEmpty()) {
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(key);
            editor.commit();
        }
    }

    public static void clearPreferences(Context context, String alias) {
        SharedPreferences pref = context.getSharedPreferences(alias, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
