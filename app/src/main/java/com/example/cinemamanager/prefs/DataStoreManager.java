package com.example.cinemamanager.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.cinemamanager.constant.ConstantKey;
import com.example.cinemamanager.model.User;
import com.google.gson.Gson;

public class DataStoreManager {

    private static final String PREF_NAME = "CinemaPrefs";
    private static SharedPreferences sharedPreferences;

    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void setUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonUser = new Gson().toJson(user);
        editor.putString(ConstantKey.PREF_KEY_USER, jsonUser);
        editor.putBoolean(ConstantKey.PREF_KEY_IS_LOGGED, true);
        editor.apply();
    }

    public static User getUser() {
        String jsonUser = sharedPreferences.getString(ConstantKey.PREF_KEY_USER, null);
        if (jsonUser != null) {
            return new Gson().fromJson(jsonUser, User.class);
        }
        return null;
    }

    public static void clearUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ConstantKey.PREF_KEY_USER);
        editor.putBoolean(ConstantKey.PREF_KEY_IS_LOGGED, false);
        editor.apply();
    }

    public static boolean isLoggedIn() {
        return sharedPreferences.getBoolean(ConstantKey.PREF_KEY_IS_LOGGED, false);
    }
}
