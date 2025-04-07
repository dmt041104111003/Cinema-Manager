package com.example.cinemamanager.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cinemamanager.R;
import com.example.cinemamanager.adapter.ViewPager2Adapter;
import com.example.cinemamanager.databinding.ActivityMainBinding;
import com.example.cinemamanager.fragment.AccountFragment;
import com.example.cinemamanager.fragment.BookingFragment;
import com.example.cinemamanager.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private ViewPager2Adapter adapter;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        setupViewPager();
        initListener();
    }

    private void initView() {
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new BookingFragment());
        fragments.add(new AccountFragment());
    }

    private void setupViewPager() {
        adapter = new ViewPager2Adapter(this, fragments);
        binding.viewpager2.setAdapter(adapter);
        binding.viewpager2.setUserInputEnabled(false);
        binding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
                        binding.tvTitle.setText(R.string.nav_home);
                        break;
                    case 1:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_booking).setChecked(true);
                        binding.tvTitle.setText(R.string.nav_booking);
                        break;
                    case 2:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_account).setChecked(true);
                        binding.tvTitle.setText(R.string.nav_account);
                        break;
                }
            }
        });
    }

    private void initListener() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            binding.viewpager2.setCurrentItem(0);
            binding.tvTitle.setText(R.string.nav_home);
        } else if (id == R.id.nav_booking) {
            binding.viewpager2.setCurrentItem(1);
            binding.tvTitle.setText(R.string.nav_booking);
        } else if (id == R.id.nav_account) {
            binding.viewpager2.setCurrentItem(2);
            binding.tvTitle.setText(R.string.nav_account);
        }
        return true;
    }
}
