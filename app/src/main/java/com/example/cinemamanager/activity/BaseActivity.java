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
