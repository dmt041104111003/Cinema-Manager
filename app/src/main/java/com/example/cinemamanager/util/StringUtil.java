package com.example.cinemamanager.util;

<<<<<<< HEAD
public class StringUtil {

    public static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isEmpty(String input) {
        return input == null || input.isEmpty() || ("").equals(input.trim());
    }

    public static String getDoubleNumber(int number) {
        if (number < 10) {
            return "0" + number;
        } else return "" + number;
=======
import android.text.TextUtils;
import android.util.Patterns;

public class StringUtil {

    public static boolean isEmpty(String input) {
        return input == null || TextUtils.isEmpty(input.trim());
    }

    public static boolean isValidEmail(String email) {
        return !isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
>>>>>>> aa3ec73d255cc8b635a103114945c64efb205e9e
    }
}
