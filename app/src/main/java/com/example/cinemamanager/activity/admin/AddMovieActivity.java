package com.example.cinemamanager.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cinema.MyApplication;
import com.example.cinema.R;
import com.example.cinema.activity.BaseActivity;
import com.example.cinema.adapter.admin.AdminSelectCategoryAdapter;
import com.example.cinema.constant.ConstantKey;
import com.example.cinema.constant.GlobalFunction;
import com.example.cinema.databinding.ActivityAddMovieBinding;
import com.example.cinema.model.Category;
import com.example.cinema.model.Movie;
import com.example.cinema.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMovieActivity extends BaseActivity {

    private ActivityAddMovieBinding mActivityAddMovieBinding;
    private boolean isUpdate;
    private Movie mMovie;
    private List<Category> mListCategory;
    private Category mCategorySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAddMovieBinding = ActivityAddMovieBinding.inflate(getLayoutInflater());
        setContentView(mActivityAddMovieBinding.getRoot());

        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            isUpdate = true;
            mMovie = (Movie) bundleReceived.get(ConstantKey.KEY_INTENT_MOVIE_OBJECT);
        }

        initView();
        getListCategory();

        mActivityAddMovieBinding.imgBack.setOnClickListener(v -> onBackPressed());
        mActivityAddMovieBinding.btnAddOrEdit.setOnClickListener(v -> addOrEditMovie());
        mActivityAddMovieBinding.tvDate.setOnClickListener(v -> {
            if (isUpdate) {
                GlobalFunction.showDatePicker(AddMovieActivity.this, mMovie.getDate(), date -> mActivityAddMovieBinding.tvDate.setText(date));
            } else {
                GlobalFunction.showDatePicker(AddMovieActivity.this, "", date -> mActivityAddMovieBinding.tvDate.setText(date));
            }
        });
    }

    private void initView() {
        if (isUpdate) {
            mActivityAddMovieBinding.tvTitle.setText(getString(R.string.edit_movie_title));
            mActivityAddMovieBinding.btnAddOrEdit.setText(getString(R.string.action_edit));

            mActivityAddMovieBinding.edtName.setText(mMovie.getName());
            mActivityAddMovieBinding.edtDescription.setText(mMovie.getDescription());
            mActivityAddMovieBinding.edtPrice.setText(String.valueOf(mMovie.getPrice()));
            mActivityAddMovieBinding.tvDate.setText(mMovie.getDate());
            mActivityAddMovieBinding.edtImage.setText(mMovie.getImage());
            mActivityAddMovieBinding.edtImageBanner.setText(mMovie.getImageBanner());
            mActivityAddMovieBinding.edtVideo.setText(mMovie.getUrl());
        } else {
            mActivityAddMovieBinding.tvTitle.setText(getString(R.string.add_movie_title));
            mActivityAddMovieBinding.btnAddOrEdit.setText(getString(R.string.action_add));
        }
    }

    private void getListCategory() {
        MyApplication.get(this).getCategoryDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListCategory != null) {
                    mListCategory.clear();
                } else {
                    mListCategory = new ArrayList<>();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.getValue(Category.class);
                    mListCategory.add(0, category);
                }
                initSpinnerCategory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private int getPositionCategoryUpdate(Movie movie) {
        if (mListCategory == null || mListCategory.isEmpty()) {
            return 0;
        }
        for (int i = 0; i < mListCategory.size(); i++) {
            if (movie.getCategoryId() == mListCategory.get(i).getId()) {
                return i;
            }
        }
        return 0;
    }

    private void initSpinnerCategory() {
        AdminSelectCategoryAdapter selectCategoryAdapter = new AdminSelectCategoryAdapter(this,
                R.layout.item_choose_option, mListCategory);
        mActivityAddMovieBinding.spnCategory.setAdapter(selectCategoryAdapter);
        mActivityAddMovieBinding.spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCategorySelected = selectCategoryAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        if (isUpdate) {
            mActivityAddMovieBinding.spnCategory.setSelection(getPositionCategoryUpdate(mMovie));
        }
    }


}
