package com.example.cinemamanager.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.cinemamanager.R;

public abstract class BaseActivity extends AppCompatActivity {
    private MaterialDialog progressDialog;
    private MaterialDialog alertDialog;
    private Toast currentToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void createProgressDialog() {
        if (progressDialog == null || progressDialog.isCancelled()) {
            progressDialog = new MaterialDialog.Builder(this)
                    .content(R.string.waiting_message)
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        }
    }

    public void showProgressDialog(boolean value) {
        try {
            if (value) {
                if (progressDialog == null) {
                    createProgressDialog();
                }
                if (!isFinishing() && !isDestroyed() && !progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } else {
                dismissProgressDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAlertDialog() {
        if (alertDialog == null || alertDialog.isCancelled()) {
            alertDialog = new MaterialDialog.Builder(this)
                    .title(R.string.app_name)
                    .positiveText(R.string.action_ok)
                    .cancelable(false)
                    .build();
        }
    }

    public void showAlertDialog(String message) {
        try {
            if (alertDialog == null) {
                createAlertDialog();
            }
            if (!isFinishing() && !isDestroyed()) {
                alertDialog.setContent(message);
                alertDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast(message);
        }
    }

    public void showAlertDialog(@StringRes int resourceId) {
        showAlertDialog(getString(resourceId));
    }

    public void showToast(String message) {
        if (currentToast != null) {
            currentToast.cancel();
        }
        currentToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        currentToast.show();
    }

    public void showToast(@StringRes int resourceId) {
        showToast(getString(resourceId));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (currentToast != null) {
            currentToast.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissProgressDialog();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        if (currentToast != null) {
            currentToast.cancel();
            currentToast = null;
        }
    }
}
