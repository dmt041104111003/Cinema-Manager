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