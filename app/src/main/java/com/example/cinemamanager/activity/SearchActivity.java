package com.example.cinemamanager.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.cinemamanager.MyApplication;
import com.example.cinemamanager.R;
import com.example.cinemamanager.adapter.MovieAdapter;
import com.example.cinemamanager.constant.GlobalFunction;
import com.example.cinemamanager.databinding.ActivitySearchBinding;
import com.example.cinemamanager.model.Category;
import com.example.cinemamanager.model.Movie;
import com.example.cinemamanager.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySearchBinding mActivitySearchBinding;
    private List<Category> mListCategory = new ArrayList<>();
    private Category mCategorySelected;
    private List<Movie> mListMovies = new ArrayList<>();
    private ValueEventListener categoryListener, movieListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySearchBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(mActivitySearchBinding.getRoot());

        initListener();
        getListCategory();
    }

    private void initListener() {
        mActivitySearchBinding.imageBack.setOnClickListener(v -> {
            GlobalFunction.hideSoftKeyboard(SearchActivity.this);
            onBackPressed();
        });
        mActivitySearchBinding.imageDelete.setOnClickListener(v -> mActivitySearchBinding.edtKeyword.setText(""));
        mActivitySearchBinding.edtKeyword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMovie();
                return true;
            }
            return false;
        });
        mActivitySearchBinding.edtKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mActivitySearchBinding.imageDelete.setVisibility(s.toString().trim().isEmpty() ? View.GONE : View.VISIBLE);
                if (s.toString().trim().isEmpty()) {
                    searchMovie();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void getListCategory() {
        categoryListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mActivitySearchBinding.tvCategoryTitle.setVisibility(View.VISIBLE);
                mActivitySearchBinding.layoutCategory.setVisibility(View.VISIBLE);

                mListCategory.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.getValue(Category.class);
                    if (category != null) {
                        mListCategory.add(0, category);
                    }
                }
                mCategorySelected = new Category(0, getString(R.string.label_all), "");
                mListCategory.add(0, mCategorySelected);
                initLayoutCategory("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mActivitySearchBinding.tvCategoryTitle.setVisibility(View.GONE);
                mActivitySearchBinding.layoutCategory.setVisibility(View.GONE);
            }
        };
        MyApplication.get(this).getCategoryDatabaseReference().addValueEventListener(categoryListener);
    }

    private void initLayoutCategory(String tag) {
        mActivitySearchBinding.layoutCategory.removeAllViews();
        for (Category category : mListCategory) {
            TextView textView = createCategoryTextView(category, tag);
            mActivitySearchBinding.layoutCategory.addView(textView);
        }
    }

    private TextView createCategoryTextView(Category category, String tag) {
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 20, 10);

        TextView textView = new TextView(this);
        textView.setLayoutParams(params);
        textView.setPadding(30, 10, 30, 10);
        textView.setTag(String.valueOf(category.getId()));
        textView.setText(category.getName());
        textView.setTextSize(getResources().getDimension(R.dimen.text_size_small) /
                getResources().getDisplayMetrics().density);

        if (tag.equals(String.valueOf(category.getId()))) {
            mCategorySelected = category;
            textView.setBackgroundResource(R.drawable.bg_white_shape_round_corner_border_red);
            textView.setTextColor(getResources().getColor(R.color.red));
            searchMovie();
        } else {
            textView.setBackgroundResource(R.drawable.bg_white_shape_round_corner_border_grey);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        textView.setOnClickListener(this);
        return textView;
    }

    private void searchMovie() {
        movieListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListMovies.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    if (isMovieResult(movie)) {
                        mListMovies.add(0, movie);
                    }
                }
                displayListMoviesResult();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        MyApplication.get(this).getMovieDatabaseReference().addValueEventListener(movieListener);
    }

    private void displayListMoviesResult() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mActivitySearchBinding.rcvData.setLayoutManager(gridLayoutManager);
        MovieAdapter movieAdapter = new MovieAdapter(mListMovies,
                movie -> GlobalFunction.goToMovieDetail(this, movie));
        mActivitySearchBinding.rcvData.setAdapter(movieAdapter);
    }

    private boolean isMovieResult(Movie movie) {
        if (movie == null) return false;

        String key = mActivitySearchBinding.edtKeyword.getText().toString().trim();
        long categoryId = mCategorySelected.getId();

        boolean isMatch = StringUtil.isEmpty(key) || GlobalFunction.getTextSearch(movie.getName())
                .toLowerCase().contains(GlobalFunction.getTextSearch(key).toLowerCase());

        return categoryId == 0 ? isMatch : isMatch && movie.getCategoryId() == categoryId;
    }

    @Override
    public void onClick(View v) {
        initLayoutCategory(v.getTag().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (categoryListener != null) {
            MyApplication.get(this).getCategoryDatabaseReference().removeEventListener(categoryListener);
        }
        if (movieListener != null) {
            MyApplication.get(this).getMovieDatabaseReference().removeEventListener(movieListener);
        }
    }
}