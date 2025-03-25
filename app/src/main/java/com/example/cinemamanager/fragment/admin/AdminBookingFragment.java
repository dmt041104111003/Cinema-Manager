package com.example.cinemamanager.fragment.admin;

public class AdminBookingFragment extends Fragment {

    private FragmentAdminBookingBinding mFragmentAdminBookingBinding;
    private List<BookingHistory> mListBookingHistory;
    private BookingHistoryAdapter mBookingHistoryAdapter;
    private boolean mIsUsedChecked;
    private String mKeyWord = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminBookingBinding = FragmentAdminBookingBinding.inflate(inflater, container, false);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initListener();
        getListBookingHistory();

        return mFragmentAdminBookingBinding.getRoot();
    }

    private void initListener() {
        mFragmentAdminBookingBinding.imgSearch.setOnClickListener(view1 -> searchBooking());
        mFragmentAdminBookingBinding.chbBookingUsed.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mIsUsedChecked = isChecked;
            getListBookingHistory();
        });

        mFragmentAdminBookingBinding.edtSearchId.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchBooking();
                return true;
            }
            return false;
        });

        mFragmentAdminBookingBinding.edtSearchId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    mKeyWord = "";
                    getListBookingHistory();
                }
            }
        });
        mFragmentAdminBookingBinding.imgScanQr.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                clickOpenScanQRCode();
            }
        });
    }

    private void searchBooking() {
        mKeyWord = mFragmentAdminBookingBinding.edtSearchId.getText().toString().trim();
        getListBookingHistory();
        GlobalFunction.hideSoftKeyboard(getActivity());
    }

    public void getListBookingHistory() {
        if (getActivity() == null) {
            return;
        }
        MyApplication.get(getActivity()).getBookingDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListBookingHistory != null) {
                    mListBookingHistory.clear();
                } else {
                    mListBookingHistory = new ArrayList<>();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookingHistory bookingHistory = dataSnapshot.getValue(BookingHistory.class);
                    if (bookingHistory != null) {
                        boolean isExpire = DateTimeUtils.convertDateToTimeStamp(bookingHistory.getDate()) < DateTimeUtils.getLongCurrentTimeStamp();
                        if (mIsUsedChecked) {
                            if (isExpire || bookingHistory.isUsed()) {
                                if (StringUtil.isEmpty(mKeyWord)) {
                                    mListBookingHistory.add(0, bookingHistory);
                                } else {
                                    if (String.valueOf(bookingHistory.getId()).contains(mKeyWord)) {
                                        mListBookingHistory.add(0, bookingHistory);
                                    }
                                }
                            }
                        } else {
                            if (!isExpire && !bookingHistory.isUsed()) {
                                if (StringUtil.isEmpty(mKeyWord)) {
                                    mListBookingHistory.add(0, bookingHistory);
                                } else {
                                    if (String.valueOf(bookingHistory.getId()).contains(mKeyWord)) {
                                        mListBookingHistory.add(0, bookingHistory);
                                    }
                                }
                            }
                        }
                    }
                }
                displayListBookingHistory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void displayListBookingHistory() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminBookingBinding.rcvBookingHistory.setLayoutManager(linearLayoutManager);

        mBookingHistoryAdapter = new BookingHistoryAdapter(getActivity(), true,
                mListBookingHistory, null, this::updateStatusBooking);
        mFragmentAdminBookingBinding.rcvBookingHistory.setAdapter(mBookingHistoryAdapter);
    }

    private void updateStatusBooking(String id) {
        if (getActivity() == null) {
            return;
        }
        MyApplication.get(getActivity()).getBookingDatabaseReference().child(id).child("used").setValue(true,
                (error, ref) -> Toast.makeText(getActivity(), getString(R.string.msg_confirm_booking_success), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBookingHistoryAdapter != null) mBookingHistoryAdapter.release();
    }

    private void clickOpenScanQRCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setPrompt("Quét mã order vé xem phim");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ResultQrCodeEvent event) {
        if (event != null) {
            mFragmentAdminBookingBinding.edtSearchId.setText(event.getResult());
            mKeyWord = event.getResult();
            getListBookingHistory();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
