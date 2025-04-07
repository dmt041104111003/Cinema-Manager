package com.example.cinemamanager.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cinema.MyApplication;
import com.example.cinema.R;
import com.example.cinema.adapter.FoodDrinkAdapter;
import com.example.cinema.adapter.RoomAdapter;
import com.example.cinema.adapter.SeatAdapter;
import com.example.cinema.adapter.SelectPaymentAdapter;
import com.example.cinema.adapter.TimeAdapter;
import com.example.cinema.constant.ConstantKey;
import com.example.cinema.constant.GlobalFunction;
import com.example.cinema.constant.PayPalConfig;
import com.example.cinema.databinding.ActivityConfirmBookingBinding;
import com.example.cinema.listener.IOnSingleClickListener;
import com.example.cinema.model.BookingHistory;
import com.example.cinema.model.Food;
import com.example.cinema.model.Movie;
import com.example.cinema.model.PaymentMethod;
import com.example.cinema.model.Room;
import com.example.cinema.model.RoomFirebase;
import com.example.cinema.model.Seat;
import com.example.cinema.model.SeatLocal;
import com.example.cinema.model.SlotTime;
import com.example.cinema.model.TimeFirebase;
import com.example.cinema.prefs.DataStoreManager;
import com.example.cinema.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ConfirmBookingActivity extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE = 199;
    public static final String PAYPAL_PAYMENT_STATUS_APPROVED = "approved";
    //Paypal Configuration Object
    public static final PayPalConfiguration PAYPAL_CONFIG = new PayPalConfiguration()
            .environment(PayPalConfig.PAYPAL_ENVIRONMENT_DEV)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID_DEV)
            .acceptCreditCards(false);
    private Dialog mDialog;

    private ActivityConfirmBookingBinding mActivityConfirmBookingBinding;
    private Movie mMovie;

    private List<Room> mListRooms;
    private RoomAdapter mRoomAdapter;
    private String mTitleRoomSelected;

    private List<SlotTime> mListTimes;
    private TimeAdapter mTimeAdapter;
    private String mTitleTimeSelected;

    private List<Food> mListFood;
    private FoodDrinkAdapter mFoodDrinkAdapter;

    private List<SeatLocal> mListSeats;
    private SeatAdapter mSeatAdapter;

    private PaymentMethod mPaymentMethodSelected;
    private BookingHistory mBookingHistory;

    private List<Food> mListFoodNeedUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityConfirmBookingBinding = ActivityConfirmBookingBinding.inflate(getLayoutInflater());
        setContentView(mActivityConfirmBookingBinding.getRoot());

        getDataIntent();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        Movie movie = (Movie) bundle.get(ConstantKey.KEY_INTENT_MOVIE_OBJECT);
        getMovieInformation(movie.getId());
    }

    private void getMovieInformation(long movieId) {
        MyApplication.get(this).getMovieDatabaseReference().child(String.valueOf(movieId))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mMovie = snapshot.getValue(Movie.class);

                        displayDataMovie();
                        initListener();
                        initSpinnerCategory();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void displayDataMovie() {
        if (mMovie == null) {
            return;
        }
        mActivityConfirmBookingBinding.tvMovieName.setText(mMovie.getName());
        String strPrice = mMovie.getPrice() + ConstantKey.UNIT_CURRENCY_MOVIE;
        mActivityConfirmBookingBinding.tvMoviePrice.setText(strPrice);

        showListRooms();
        initListFoodAndDrink();
    }

    private void initListener() {
        mActivityConfirmBookingBinding.imgBack.setOnClickListener(view -> onBackPressed());
        mActivityConfirmBookingBinding.btnConfirm.setOnClickListener(view -> onClickBookingMovie());
    }

    private void showListRooms() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mActivityConfirmBookingBinding.rcvRoom.setLayoutManager(gridLayoutManager);

        mListRooms = getListRoomLocal();
        mRoomAdapter = new RoomAdapter(mListRooms, this::onClickSelectRoom);
        mActivityConfirmBookingBinding.rcvRoom.setAdapter(mRoomAdapter);
    }

    private List<Room> getListRoomLocal() {
        List<Room> list = new ArrayList<>();
        if (mMovie.getRooms() != null) {
            for (RoomFirebase roomFirebase : mMovie.getRooms()) {
                Room room = new Room(roomFirebase.getId(), roomFirebase.getTitle(), false);
                list.add(room);
            }
        }
        return list;
    }




}
