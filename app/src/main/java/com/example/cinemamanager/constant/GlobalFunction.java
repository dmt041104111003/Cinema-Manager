package com.example.cinemamanager.constant;

import android.app.Activity;
import android.content.Intent;

import com.example.cinemamanager.activity.MainActivity;

public class GlobalFunction {

    public static void gotoMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, Class<?> clz) {
        Intent intent = new Intent(activity, clz);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, Class<?> clz, String key, Object value) {
        Intent intent = new Intent(activity, clz);
        if (value instanceof String) {
            intent.putExtra(key, (String) value);
        } else if (value instanceof Integer) {
            intent.putExtra(key, (Integer) value);
        } else if (value instanceof Boolean) {
            intent.putExtra(key, (Boolean) value);
        }
        activity.startActivity(intent);
    }
}
