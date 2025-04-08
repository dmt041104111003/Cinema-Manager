package com.example.cinemamanager;

import android.app.Application;
<<<<<<< HEAD
import android.content.Context;

public class MyApplication extends Application {

    private static final String FIREBASE_URL = "https://cinemamanager-1ceee-default-rtdb.firebaseio.com";

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DataStoreManager.init(getApplicationContext());
        FirebaseApp.initializeApp(this);
    }

    public DatabaseReference getFoodDatabaseReference() {
        return FirebaseDatabase.getInstance(FIREBASE_URL).getReference("/food");
    }

    public DatabaseReference getCategoryDatabaseReference() {
        return FirebaseDatabase.getInstance(FIREBASE_URL).getReference("/category");
    }

    public DatabaseReference getMovieDatabaseReference() {
        return FirebaseDatabase.getInstance(FIREBASE_URL).getReference("/movie");
    }

    public DatabaseReference getBookingDatabaseReference() {
        return FirebaseDatabase.getInstance(FIREBASE_URL).getReference("/booking");
    }

    public DatabaseReference getQuantityDatabaseReference(long foodId) {
        return FirebaseDatabase.getInstance().getReference("/food/" + foodId + "/quantity");
=======

import com.example.cinemamanager.prefs.DataStoreManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
   
        DataStoreManager.init(this);
>>>>>>> aa3ec73d255cc8b635a103114945c64efb205e9e
    }
}
