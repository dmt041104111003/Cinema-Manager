package com.example.cinemamanager.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.cinemamanager.R;

import java.lang.ref.WeakReference;

public abstract class BaseActivity extends AppCompatActivity {
    private MaterialDialog progressDialog;
    private MaterialDialog alertDialog;
    private Toast currentToast;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final long DIALOG_DISMISS_DELAY = 500L;
    private WeakReference<MaterialDialog> activeDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void createProgressDialog() {
        if (isFinishing() || isDestroyed()) {
            return;
        }

        if (progressDialog == null || progressDialog.isCancelled()) {
            progressDialog = new MaterialDialog.Builder(this)
                    .content(R.string.waiting_message)
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        }
    }

    public void showProgressDialog(boolean value) {
        if (isFinishing() || isDestroyed()) {
            return;
        }

        mainHandler.post(() -> {
            try {
                if (value) {
                    if (progressDialog == null) {
                        createProgressDialog();
                    }
                    if (!progressDialog.isShowing()) {
                        progressDialog.show();
                        activeDialog = new WeakReference<>(progressDialog);
                    }
                } else {
                    dismissProgressDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void dismissProgressDialog() {
        mainHandler.post(() -> {
            try {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    mainHandler.postDelayed(() -> progressDialog = null, DIALOG_DISMISS_DELAY);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void createAlertDialog() {
        if (isFinishing() || isDestroyed()) {
            return;
        }

        if (alertDialog == null || alertDialog.isCancelled()) {
            alertDialog = new MaterialDialog.Builder(this)
                    .title(R.string.app_name)
                    .positiveText(R.string.action_ok)
                    .onPositive((dialog, which) -> handleDialogButtonClick(DialogAction.POSITIVE))
                    .cancelable(false)
                    .build();
        }
    }

    protected void handleDialogButtonClick(@NonNull DialogAction action) {
        // Override in child activities if needed
    }

    public void showAlertDialog(String message) {
        if (isFinishing() || isDestroyed()) {
            return;
        }

        mainHandler.post(() -> {
            try {
                if (alertDialog == null) {
                    createAlertDialog();
                }
                alertDialog.setContent(message);
                if (!alertDialog.isShowing()) {
                    alertDialog.show();
                    activeDialog = new WeakReference<>(alertDialog);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast(message);
            }
        });
    }

    public void showAlertDialog(@StringRes int resourceId) {
        showAlertDialog(getString(resourceId));
    }

    public void showToast(String message) {
        if (isFinishing() || isDestroyed()) {
            return;
        }

        mainHandler.post(() -> {
            try {
                if (currentToast != null) {
                    currentToast.cancel();
                }
                currentToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                currentToast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void showToast(@StringRes int resourceId) {
        showToast(getString(resourceId));
    }

    private void dismissActiveDialog() {
        MaterialDialog dialog = activeDialog != null ? activeDialog.get() : null;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        activeDialog = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainHandler.post(() -> {
            if (currentToast != null) {
                currentToast.cancel();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissProgressDialog();
        dismissActiveDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainHandler.removeCallbacksAndMessages(null);
        dismissProgressDialog();
        dismissActiveDialog();
        
        if (currentToast != null) {
            currentToast.cancel();
            currentToast = null;
        }

        progressDialog = null;
        alertDialog = null;
    }
}
