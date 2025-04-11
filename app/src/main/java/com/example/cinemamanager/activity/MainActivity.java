package com.example.cinemamanager.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.cinemamanager.R;
import com.example.cinemamanager.adapter.MyViewPagerAdapter;
import com.example.cinemamanager.databinding.ActivityMainBinding;
import com.example.cinemamanager.prefs.DataStoreManager;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding activityMainBinding;
    private long backPressedTime;
    private static final long BACK_PRESS_DELAY = 2000;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt("current_page", 0);
        }

        initializeViewPager();
        setupBottomNavigation();
    }

    private void initializeViewPager() {
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(this);
        activityMainBinding.viewpager2.setAdapter(myViewPagerAdapter);
        activityMainBinding.viewpager2.setUserInputEnabled(false);
        activityMainBinding.viewpager2.setCurrentItem(currentPage, false);

        activityMainBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                updateNavigationUI(position);
            }
        });
    }

    private void setupBottomNavigation() {
        activityMainBinding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                switchPage(0);
            } else if (id == R.id.nav_booking) {
                switchPage(1);
            } else if (id == R.id.nav_user) {
                switchPage(2);
            }
            return true;
        });
    }

    private void switchPage(int position) {
        activityMainBinding.viewpager2.setCurrentItem(position, true);
        updateNavigationUI(position);
    }

    private void updateNavigationUI(int position) {
        switch (position) {
            case 0:
                activityMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
                activityMainBinding.tvTitle.setText(getString(R.string.nav_home));
                break;
            case 1:
                activityMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_booking).setChecked(true);
                activityMainBinding.tvTitle.setText(getString(R.string.nav_booking));
                break;
            case 2:
                activityMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_user).setChecked(true);
                activityMainBinding.tvTitle.setText(getString(R.string.nav_user));
                break;
        }
    }

    private void showDialogLogout() {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .content(getString(R.string.msg_confirm_login_another_device))
                .positiveText(getString(R.string.action_ok))
                .negativeText(getString(R.string.action_cancel))
                .onPositive((dialog, which) -> {
                    logout();
                    dialog.dismiss();
                    finishAffinity();
                })
                .onNegative((dialog, which) -> dialog.dismiss())
                .cancelable(false)
                .show();
    }

    private void logout() {
        DataStoreManager.clearUser();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + BACK_PRESS_DELAY > System.currentTimeMillis()) {
            showDialogLogout();
        } else {
            Toast.makeText(this, getString(R.string.press_back_to_exit), Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current_page", currentPage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityMainBinding = null;
    }
}