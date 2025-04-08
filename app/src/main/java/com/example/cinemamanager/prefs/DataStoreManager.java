package com.example.cinemamanager.prefs;

import android.content.Context;
<<<<<<< HEAD

import androidx.annotation.Nullable;

public class DataStoreManager {

    public static final String PREF_USER_INFOR = "PREF_USER_INFOR";

    private static DataStoreManager instance;
    private MySharedPreferences sharedPreferences;

    public static void init(Context context) {
        instance = new DataStoreManager();
        instance.sharedPreferences = new MySharedPreferences(context);
    }

    public static DataStoreManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            throw new IllegalStateException("Not initialized");
        }
    }

    public static void setUser(@Nullable User user) {
        String jsonUser = "";
        if (user != null) {
            jsonUser = user.toJSon();
        }
        DataStoreManager.getInstance().sharedPreferences.putStringValue(PREF_USER_INFOR, jsonUser);
    }

    public static User getUser() {
        String jsonUser = DataStoreManager.getInstance().sharedPreferences.getStringValue(PREF_USER_INFOR);
        if (!StringUtil.isEmpty(jsonUser)) {
            return new Gson().fromJson(jsonUser, User.class);
        }
        return new User();
=======
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
>>>>>>> aa3ec73d255cc8b635a103114945c64efb205e9e
    }
}
