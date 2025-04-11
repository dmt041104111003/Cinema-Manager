package com.example.cinemamanager.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.cinemamanager.R;

public abstract class BaseActivity extends AppCompatActivity {
    protected MaterialDialog progressDialog, alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createProgressDialog();
        createAlertDialog();
    }

    public void createProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new MaterialDialog.Builder(this)
                    .content(R.string.waiting_message)
                    .progress(true, 0)
                    .build();
        }
    }

    public void showProgressDialog(boolean value) {
        if (progressDialog == null) {
            createProgressDialog();
        }
        if (value) {
            if (!isFinishing() && !progressDialog.isShowing()) {
                progressDialog.show();
                progressDialog.setCancelable(false);
            }
        } else {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public void createAlertDialog() {
        if (alertDialog == null) {
            alertDialog = new MaterialDialog.Builder(this)
                    .title(R.string.app_name)
                    .positiveText(R.string.action_ok)
                    .cancelable(false)
                    .build();
        }
    }

    public void showAlertDialog(String errorMessage) {
        if (alertDialog == null) {
            createAlertDialog();
        }
        if (!isFinishing()) {
            alertDialog.setContent(errorMessage);
            alertDialog.show();
        }
    }

    public void showAlertDialog(@StringRes int resourceId) {
        if (alertDialog == null) {
            createAlertDialog();
        }
        if (!isFinishing()) {
            alertDialog.setContent(resourceId);
            alertDialog.show();
        }
    }

    public void setCancelProgress(boolean isCancel) {
        if (progressDialog != null) {
            progressDialog.setCancelable(isCancel);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissProgressDialog();
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
}
