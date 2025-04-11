package com.example.cinema.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.cinemamanager.R;

public abstract class BaseActivity extends AppCompatActivity {
    
    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final int MAX_DIALOG_DURATION_MS = 30000; // 30 seconds

    @Nullable protected MaterialDialog progressDialog;
    @Nullable protected MaterialDialog alertDialog;
    private @Nullable Handler timeoutHandler;
    private boolean isActivityActive = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActivityActive = true;
        timeoutHandler = new Handler(Looper.getMainLooper());
        initializeDialogs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityActive = false;
        dismissAllDialogs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityActive = false;
        if (timeoutHandler != null) {
            timeoutHandler.removeCallbacksAndMessages(null);
            timeoutHandler = null;
        }
        dismissAllDialogs();
    }

    private void initializeDialogs() {
        try {
            if (!isFinishing()) {
                createProgressDialog();
                createAlertDialog();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing dialogs", e);
        }
    }

    protected void createProgressDialog() {
        if (isActivityActive && !isFinishing()) {
            progressDialog = new MaterialDialog.Builder(this)
                    .content(R.string.waiting_message)
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        }
    }

    public void showProgressDialog(final boolean show) {
        if (!isActivityActive) return;

        runOnUiThread(() -> {
            try {
                if (show) {
                    if (progressDialog == null || progressDialog.isShowing()) {
                        createProgressDialog();
                    }
                    if (progressDialog != null && !progressDialog.isShowing() && !isFinishing()) {
                        progressDialog.show();
                        startDialogTimeout();
                    }
                } else {
                    dismissProgressDialog();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error showing progress dialog", e);
            }
        });
    }

    private void startDialogTimeout() {
        if (timeoutHandler != null) {
            timeoutHandler.removeCallbacksAndMessages(null);
            timeoutHandler.postDelayed(this::dismissProgressDialog, MAX_DIALOG_DURATION_MS);
        }
    }

    public void dismissProgressDialog() {
        if (!isActivityActive) return;

        runOnUiThread(() -> {
            try {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                progressDialog = null;
            } catch (Exception e) {
                Log.e(TAG, "Error dismissing progress dialog", e);
            }
        });
    }

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public void createAlertDialog() {
        alertDialog = new MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .positiveText(R.string.action_ok)
                .cancelable(false)
                .build();
    }

    public void showAlertDialog(String errorMessage) {
        alertDialog.setContent(errorMessage);
        alertDialog.show();
    }

    public void showAlertDialog(@StringRes int resourceId) {
        alertDialog.setContent(resourceId);
        alertDialog.show();
    }

    public void setCancelProgress(boolean isCancel) {
        if (progressDialog != null) {
            progressDialog.setCancelable(isCancel);
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
