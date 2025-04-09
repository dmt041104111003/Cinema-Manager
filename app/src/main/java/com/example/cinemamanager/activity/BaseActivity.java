<<<<<<< HEAD
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
=======
package com.example.cinemamanager.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.cinemamanager.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected MaterialDialog progressDialog;
    protected MaterialDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createProgressDialog();
    }

    public void createProgressDialog() {
        progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.waiting_message)
                .progress(true, 0)
                .cancelable(false)
                .build();
    }

    public void showProgressDialog(boolean value) {
        if (value) {
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public void createAlertDialog(String title, String message) {
        alertDialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .positiveText(R.string.action_ok)
                .cancelable(false)
                .build();
    }

    public void createAlertDialog(@StringRes int title, @StringRes int message) {
        alertDialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .positiveText(R.string.action_ok)
                .cancelable(false)
                .build();
    }

    public void showAlertDialog() {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        super.onDestroy();
    }
}
>>>>>>> aa3ec73d255cc8b635a103114945c64efb205e9e
