package com.example.cinema.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.cinemamanager.MyApplication;
import com.example.cinemamanager.adapter.MovieAdapter;
import com.example.cinemamanager.constant.ConstantKey;
import com.example.cinemamanager.constant.GlobalFunction;
import com.example.cinemamanager.databinding.ActivityCategoryBinding;
import com.example.cinemamanager.model.Category;
import com.example.cinemamanager.model.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding mActivityCategoryBinding;
    private List<Movie> mListMovies = new ArrayList<>();
    private Category mCategory;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityCategoryBinding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(mActivityCategoryBinding.getRoot());

        if (!getDataIntent()) {
            finish(); // Close the activity if no category is provided
            return;
        }

        initListener();
        setupRecyclerView();
        getListMovies();
    }

    private boolean getDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            mCategory = (Category) bundleReceived.get(ConstantKey.KEY_INTENT_CATEGORY_OBJECT);
            if (mCategory != null) {
                mActivityCategoryBinding.tvTitle.setText(mCategory.getName());
                return true;
            }
        }
        Log.e("CategoryActivity", "Category data is missing or null");
        return false;
    }

    private void initListener() {
        mActivityCategoryBinding.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mActivityCategoryBinding.rcvData.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter(mListMovies, movie -> GlobalFunction.goToMovieDetail(this, movie));
        mActivityCategoryBinding.rcvData.setAdapter(movieAdapter);
    }

    private void getListMovies() {
        MyApplication.get(this).getMovieDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListMovies.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    if (movie != null && mCategory != null && mCategory.getId() == movie.getCategoryId()) {
                        mListMovies.add(0, movie);
                    }
                }
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CategoryActivity", "Failed to fetch movies: " + error.getMessage());
            }
        });
    }
}