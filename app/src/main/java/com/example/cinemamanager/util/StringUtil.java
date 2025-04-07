package com.example.cinemamanager.util;

import android.text.TextUtils;
import android.util.Patterns;

public class StringUtil {

    public static boolean isEmpty(String input) {
        return input == null || TextUtils.isEmpty(input.trim());
    }

    public static boolean isValidEmail(String email) {
        return !isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
