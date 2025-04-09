package com.example.cinemamanager.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    private static final String DEFAULT_FORMAT_DATE = "dd-MM-yyyy";
    private static final String DEFAULT_FORMAT_DATE_2 = "dd/MM/yyyy, hh:mm a";

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
    public static long getLongCurrentTimeStamp() {
        return convertDateToTimeStamp(getDateToday());
    }

    public static long convertDateToTimeStamp(String strDate) {
        String result = "0";
        if (strDate != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT_DATE, Locale.ENGLISH);
                Date date = format.parse(strDate);
                if (date != null) {
                    long timestamp = date.getTime() / 1000;
                    result = String.valueOf(timestamp);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return Long.parseLong(result);
    }

    public static String convertTimeStampToDate(String strTimeStamp) {
        String result = "";
        if (strTimeStamp != null) {
            try {
                float floatTimestamp = Float.parseFloat(strTimeStamp);
                long timestamp = (long) floatTimestamp;
                SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_DATE_2, Locale.ENGLISH);
                Date date = (new Date(timestamp));
                result = sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
