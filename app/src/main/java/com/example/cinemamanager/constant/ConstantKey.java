package com.example.cinemamanager.constant;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ConstantKey {

    private ConstantKey() {
        // Private constructor to prevent instantiation
    }

    // Email constants
    public static final String ADMIN_EMAIL_DOMAIN = "@admin.com";
    public static final int MIN_EMAIL_LENGTH = 5;
    public static final int MAX_EMAIL_LENGTH = 50;

    // Currency constants
    public static final String CURRENCY_CODE = "VND";
    public static final String CURRENCY_SYMBOL = "₫";
    public static final int CURRENCY_MULTIPLIER = 1000;
    public static final String CURRENCY_FORMAT = "%,d %s";
    public static final String TICKET_PRICE_FORMAT = "%,d %s / vé";

    // Payment constants
    @IntDef({PAYMENT_CASH, PAYMENT_PAYPAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PaymentMethod {}

    public static final int PAYMENT_CASH = 1;
    public static final int PAYMENT_PAYPAL = 2;

    @StringDef({PAYMENT_CASH_TITLE, PAYMENT_PAYPAL_TITLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PaymentMethodTitle {}

    public static final String PAYMENT_CASH_TITLE = "Tiền mặt";
    public static final String PAYMENT_PAYPAL_TITLE = "PayPal";

    // Intent extra keys
    @StringDef({KEY_FOOD, KEY_CATEGORY, KEY_MOVIE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IntentKey {}

    public static final String KEY_FOOD = "food_object";
    public static final String KEY_CATEGORY = "category_object";
    public static final String KEY_MOVIE = "movie_object";

    // Format currency value
    public static String formatCurrency(long amount) {
        return String.format(CURRENCY_FORMAT, amount * CURRENCY_MULTIPLIER, CURRENCY_SYMBOL);
    }

    // Format ticket price
    public static String formatTicketPrice(long price) {
        return String.format(TICKET_PRICE_FORMAT, price * CURRENCY_MULTIPLIER, CURRENCY_SYMBOL);
    }

    // Check if email is admin
    public static boolean isAdminEmail(String email) {
        return email != null && email.endsWith(ADMIN_EMAIL_DOMAIN) &&
               email.length() > MIN_EMAIL_LENGTH && email.length() <= MAX_EMAIL_LENGTH;
    }

    // Get payment method title
    @PaymentMethodTitle
    public static String getPaymentMethodTitle(@PaymentMethod int method) {
        switch (method) {
            case PAYMENT_CASH:
                return PAYMENT_CASH_TITLE;
            case PAYMENT_PAYPAL:
                return PAYMENT_PAYPAL_TITLE;
            default:
                throw new IllegalArgumentException("Invalid payment method: " + method);
        }
    }
}
