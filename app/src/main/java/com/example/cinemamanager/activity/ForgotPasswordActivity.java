package com.example.cinemamanager.activity;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.cinemamanager.R;
import com.example.cinemamanager.databinding.ActivityForgotPasswordBinding;
import com.example.cinemamanager.util.StringUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseNetworkException;

public class ForgotPasswordActivity extends BaseActivity {

    private ActivityForgotPasswordBinding binding;
    private FirebaseAuth firebaseAuth;
    private static final int MAX_RESET_ATTEMPTS = 3;
    private int resetAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeFirebase();
        setupClickListeners();
    }

    private void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setupClickListeners() {
        binding.imgBack.setOnClickListener(v -> onBackPressed());
        binding.btnResetPassword.setOnClickListener(v -> onClickValidateResetPassword());
    }

    private void onClickValidateResetPassword() {
        if (resetAttempts >= MAX_RESET_ATTEMPTS) {
            showAlertDialog(R.string.msg_too_many_reset_attempts);
            return;
        }

        String email = binding.edtEmail.getText().toString().trim();
        if (!validateInput(email)) {
            return;
        }

        resetPassword(email);
    }

    private boolean validateInput(String email) {
        if (TextUtils.isEmpty(email)) {
            showToast(R.string.msg_email_require);
            binding.edtEmail.requestFocus();
            return false;
        }

        if (!StringUtil.isValidEmail(email)) {
            showToast(R.string.msg_email_invalid);
            binding.edtEmail.requestFocus();
            return false;
        }

        return true;
    }

    private void resetPassword(String email) {
        showProgressDialog(true);
        binding.btnResetPassword.setEnabled(false);

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    showProgressDialog(false);
                    binding.btnResetPassword.setEnabled(true);

                    if (task.isSuccessful()) {
                        handleSuccessfulReset();
                    } else {
                        handleResetError(task.getException());
                        resetAttempts++;
                    }
                });
    }

    private void handleSuccessfulReset() {
        showAlertDialog(R.string.msg_reset_password_successfully);
        binding.edtEmail.setText("");
        finish();
    }

    private void handleResetError(@NonNull Exception exception) {
        if (exception instanceof FirebaseAuthInvalidUserException) {
            showToast(R.string.msg_user_not_found);
        } else if (exception instanceof FirebaseNetworkException) {
            showToast(R.string.msg_network_error);
        } else {
            showToast(R.string.msg_reset_password_error);
        }

        if (resetAttempts >= MAX_RESET_ATTEMPTS - 1) {
            showAlertDialog(R.string.msg_reset_attempts_exceeded);
            binding.btnResetPassword.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
}