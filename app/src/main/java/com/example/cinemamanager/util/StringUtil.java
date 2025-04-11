package com.example.cinemamanager.util;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.Normalizer;
import java.util.regex.Pattern;

public final class StringUtil {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");
    
    private StringUtil() {
        // Private constructor to prevent instantiation
    }

    public static boolean isValidEmail(@Nullable CharSequence target) {
        return !isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidPassword(@Nullable String password) {
        return !isEmpty(password) && 
               password.length() >= MIN_PASSWORD_LENGTH &&
               PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isValidPhoneNumber(@Nullable String phone) {
        return !isEmpty(phone) && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isEmpty(@Nullable CharSequence input) {
        return TextUtils.isEmpty(input) || input.toString().trim().isEmpty();
    }

    @NonNull
    public static String formatNumber(int number) {
        return String.format("%02d", number);
    }

    @NonNull
    public static String getInitials(@Nullable String fullName) {
        if (isEmpty(fullName)) {
            return "";
        }

        StringBuilder initials = new StringBuilder();
        String[] parts = fullName.trim().split("\\s+");
        for (String part : parts) {
            if (!part.isEmpty()) {
                initials.append(Character.toUpperCase(part.charAt(0)));
            }
        }
        return initials.toString();
    }

    @NonNull
    public static String normalizeString(@Nullable String input) {
        if (isEmpty(input)) {
            return "";
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }

    @NonNull
    public static String capitalizeWords(@Nullable String input) {
        if (isEmpty(input)) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                result.append(c);
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }
}
