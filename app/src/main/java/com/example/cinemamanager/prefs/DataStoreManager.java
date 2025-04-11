package com.example.cinemamanager.prefs;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.cinemamanager.model.User;
import com.example.cinemamanager.util.StringUtil;
import com.google.gson.Gson;

public class DataStoreManager {

    public static final String PREF_USER_INFOR = "PREF_USER_INFOR";
    private static final String TAG = DataStoreManager.class.getSimpleName();

    private static volatile DataStoreManager instance;
    private MySharedPreferences sharedPreferences;

    private DataStoreManager() {
        // Private constructor to prevent instantiation
    }

    public static void init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        if (instance == null) {
            synchronized (DataStoreManager.class) {
                if (instance == null) {
                    instance = new DataStoreManager();
                    instance.sharedPreferences = new MySharedPreferences(context.getApplicationContext());
                }
            }
        }
    }

    public static DataStoreManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DataStoreManager is not initialized. Call init(Context) first.");
        }
        return instance;
    }

    public static void setUser(@Nullable User user) {
        String jsonUser = "";
        if (user != null) {
            jsonUser = user.toJSon();
        }
        DataStoreManager.getInstance().sharedPreferences.putStringValue(PREF_USER_INFOR, jsonUser);
    }

    @Nullable
    public static User getUser() {
        String jsonUser = DataStoreManager.getInstance().sharedPreferences.getStringValue(PREF_USER_INFOR);
        if (!StringUtil.isEmpty(jsonUser)) {
            try {
                return new Gson().fromJson(jsonUser, User.class);
            } catch (Exception e) {
                clearUser();
                return null;
            }
        }
        return null;
    }

    public static void clearUser() {
        DataStoreManager.getInstance().sharedPreferences.removeKey(PREF_USER_INFOR);
    }

    public static boolean isUserLoggedIn() {
        return getUser() != null;
    }
}
