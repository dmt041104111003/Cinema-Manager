package com.example.cinemamanager.constant;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cinemamanager.R;
import com.example.cinemamanager.activity.MainActivity;
import com.example.cinemamanager.activity.MovieDetailActivity;
import com.example.cinemamanager.activity.admin.AdminMainActivity;
import com.example.cinemamanager.listener.IGetDateListener;
import com.example.cinemamanager.model.Movie;
import com.example.cinemamanager.model.RoomFirebase;
import com.example.cinemamanager.model.Seat;
import com.example.cinemamanager.model.TimeFirebase;
import com.example.cinemamanager.model.User;
import com.example.cinemamanager.prefs.DataStoreManager;
import com.example.cinemamanager.util.StringUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public final class GlobalFunction {

    private static final int QR_CODE_SIZE = 512;
    private static final int MIN_SEAT_NUMBER = 1;
    private static final int MAX_SEAT_NUMBER = 18;
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    private GlobalFunction() {
        // Private constructor to prevent instantiation
    }

    public static void startActivity(@NonNull Context context, @NonNull Class<?> clz) {
        startActivity(context, clz, null);
    }

    public static void startActivity(@NonNull Context context, @NonNull Class<?> clz, @Nullable Bundle bundle) {
        Intent intent = new Intent(context, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String getTextSearch(@Nullable String input) {
        if (input == null) {
            return "";
        }
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").toLowerCase();
    }

    public static void hideSoftKeyboard(@NonNull Activity activity) {
        View focusedView = activity.getCurrentFocus();
        if (focusedView == null) {
            return;
        }

        mainHandler.post(() -> {
            try {
                InputMethodManager inputMethodManager = (InputMethodManager) activity
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void gotoMainActivity(@NonNull Context context) {
        User user = DataStoreManager.getUser();
        if (user == null) {
            return;
        }

        Class<?> targetActivity = user.isAdmin() ? AdminMainActivity.class : MainActivity.class;
        startActivity(context, targetActivity);
    }

    public static void goToMovieDetail(@NonNull Context context, @NonNull Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantKey.KEY_INTENT_MOVIE_OBJECT, movie);
        startActivity(context, MovieDetailActivity.class, bundle);
    }

    @NonNull
    public static List<RoomFirebase> getListRooms() {
        List<RoomFirebase> list = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            list.add(new RoomFirebase(i, context.getString(R.string.room_name_format, i), getListTimes()));
        }
        return list;
    }

    @NonNull
    public static List<TimeFirebase> getListTimes() {
        List<TimeFirebase> list = new ArrayList<>();
        String[] timeSlots = {
            "7AM - 8AM", "8AM - 9AM", "9AM - 10AM",
            "10AM - 11AM", "1PM - 2PM", "2PM - 3PM"
        };

        for (int i = 0; i < timeSlots.length; i++) {
            list.add(new TimeFirebase(i + 1, timeSlots[i], getListSeats()));
        }
        return list;
    }

    @NonNull
    public static List<Seat> getListSeats() {
        List<Seat> list = new ArrayList<>();
        for (int i = MIN_SEAT_NUMBER; i <= MAX_SEAT_NUMBER; i++) {
            list.add(new Seat(i, String.valueOf(i), false));
        }
        return list;
    }

    public static void showDatePicker(@NonNull Context context, @Nullable String currentDate,
            @NonNull final IGetDateListener getDateListener) {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DATE);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        if (!StringUtil.isEmpty(currentDate)) {
            try {
                String[] split = currentDate.split("-");
                if (split.length == 3) {
                    currentDay = Integer.parseInt(split[0]);
                    currentMonth = Integer.parseInt(split[1]) - 1;
                    currentYear = Integer.parseInt(split[2]);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        calendar.set(currentYear, currentMonth, currentDay);
        DatePickerDialog.OnDateSetListener callback = (view, year, monthOfYear, dayOfMonth) -> {
            String date = String.format("%02d-%02d-%d", dayOfMonth, monthOfYear + 1, year);
            getDateListener.getDate(date);
        };

        DatePickerDialog datePicker = new DatePickerDialog(context, callback,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE));

        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        datePicker.show();
    }

    public static void generateQRCode(@Nullable ImageView imageView, @Nullable String content) {
        if (imageView == null || content == null) {
            return;
        }

        mainHandler.post(() -> {
            try {
                Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
                hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                hints.put(EncodeHintType.MARGIN, 1);

                BitMatrix result = new MultiFormatWriter().encode(
                        content,
                        BarcodeFormat.QR_CODE,
                        QR_CODE_SIZE,
                        QR_CODE_SIZE,
                        hints
                );

                int width = result.getWidth();
                int height = result.getHeight();
                int[] pixels = new int[width * height];

                for (int y = 0; y < height; y++) {
                    int offset = y * width;
                    for (int x = 0; x < width; x++) {
                        pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                    }
                }

                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
                imageView.setImageBitmap(bitmap);

            } catch (WriterException e) {
                e.printStackTrace();
            }
        });
    }
}
