package com.example.cinemamanager.activity;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.cinemamanager.R;
import com.example.cinemamanager.databinding.ActivityChangePasswordBinding;
import com.example.cinemamanager.model.User;
import com.example.cinemamanager.prefs.DataStoreManager;
import com.example.cinemamanager.util.StringUtil;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends BaseActivity {

    private ActivityChangePasswordBinding binding;
    private FirebaseAuth firebaseAuth;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeFirebase();
        setupClickListeners();
    }

    private void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setupClickListeners() {
        binding.imgBack.setOnClickListener(v -> onBackPressed());
        binding.btnChangePassword.setOnClickListener(v -> onClickValidateChangePassword());
    }

    private void onClickValidateChangePassword() {
        String oldPassword = binding.edtOldPassword.getText().toString().trim();
        String newPassword = binding.edtNewPassword.getText().toString().trim();
        String confirmPassword = binding.edtConfirmPassword.getText().toString().trim();

        if (!validateInputs(oldPassword, newPassword, confirmPassword)) {
            return;
        }

        authenticateAndChangePassword(oldPassword, newPassword);
    }

    private boolean validateInputs(String oldPassword, String newPassword, String confirmPassword) {
        if (TextUtils.isEmpty(oldPassword)) {
            showToast(R.string.msg_old_password_require);
            binding.edtOldPassword.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(newPassword)) {
            showToast(R.string.msg_new_password_require);
            binding.edtNewPassword.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            showToast(R.string.msg_confirm_password_require);
            binding.edtConfirmPassword.requestFocus();
            return false;
        }

        if (newPassword.length() < MIN_PASSWORD_LENGTH || newPassword.length() > MAX_PASSWORD_LENGTH) {
            showToast(R.string.msg_password_length_invalid);
            binding.edtNewPassword.requestFocus();
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            showToast(R.string.msg_confirm_password_invalid);
            binding.edtConfirmPassword.requestFocus();
            return false;
        }

        if (oldPassword.equals(newPassword)) {
            showToast(R.string.msg_new_password_invalid);
            binding.edtNewPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void authenticateAndChangePassword(String oldPassword, String newPassword) {
        showProgressDialog(true);
        binding.btnChangePassword.setEnabled(false);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null || user.getEmail() == null) {
            handleError(new Exception(getString(R.string.msg_user_not_found)));
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        changePassword(user, newPassword);
                    } else {
                        handleError(task.getException());
                    }
                });
    }

    private void changePassword(FirebaseUser user, String newPassword) {
        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    showProgressDialog(false);
                    binding.btnChangePassword.setEnabled(true);

                    if (task.isSuccessful()) {
                        handleSuccessfulChange(newPassword);
                    } else {
                        handleError(task.getException());
                    }
                });
    }

    private void handleSuccessfulChange(String newPassword) {
        showAlertDialog(R.string.msg_change_password_successfully);
        clearInputFields();
        updateUserPassword(newPassword);
        finish();
    }

    private void handleError(@NonNull Exception exception) {
        showProgressDialog(false);
        binding.btnChangePassword.setEnabled(true);

        if (exception instanceof FirebaseAuthWeakPasswordException) {
            showToast(R.string.msg_password_weak);
        } else if (exception instanceof FirebaseAuthRecentLoginRequiredException) {
            showToast(R.string.msg_relogin_required);
            DataStoreManager.clearUser();
            GlobalFunction.startActivity(this, SignInActivity.class);
            finishAffinity();
        } else if (exception instanceof FirebaseNetworkException) {
            showToast(R.string.msg_network_error);
        } else {
            showToast(R.string.msg_change_password_error);
        }
    }

    private void clearInputFields() {
        binding.edtOldPassword.setText("");
        binding.edtNewPassword.setText("");
        binding.edtConfirmPassword.setText("");
    }

    private void updateUserPassword(String newPassword) {
        User userLogin = DataStoreManager.getUser();
        if (userLogin != null) {
            userLogin.setPassword(newPassword);
            DataStoreManager.setUser(userLogin);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
}