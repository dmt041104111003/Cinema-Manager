package com.example.cinemamanager;

import android.app.Application;

import com.example.cinemamanager.prefs.DataStoreManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
   
        DataStoreManager.init(this);
    }
}
