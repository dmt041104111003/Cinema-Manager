package com.example.cinemamanager.activity;

import static com.example.cinemamanager.constant.ConstantKey.ADMIN_EMAIL_FORMAT;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.cinemamanager.R;
import com.example.cinemamanager.constant.GlobalFunction;
import com.example.cinemamanager.databinding.ActivitySignInBinding;
import com.example.cinemamanager.model.User;
import com.example.cinemamanager.prefs.DataStoreManager;
import com.example.cinemamanager.util.StringUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends BaseActivity {

    private ActivitySignInBinding binding;
    private FirebaseAuth firebaseAuth;
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private int loginAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeFirebase();
        setupClickListeners();
        restoreLoginState();

        binding.rdbUser.setChecked(true);
        binding.layoutSignUp.setOnClickListener(
                v -> GlobalFunction.startActivity(SignInActivity.this, SignUpActivity.class));
        binding.tvForgotPassword.setOnClickListener(v -> onClickForgotPassword());
    }

    private void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setupClickListeners() {
        binding.btnSignIn.setOnClickListener(v -> onClickValidateSignIn());
    }

    private void restoreLoginState() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            GlobalFunction.gotoMainActivity(this);
            finish();
        }
    }

    private void onClickValidateSignIn() {
        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            showAlertDialog(R.string.msg_too_many_attempts);
            return;
        }

        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        boolean isAdmin = binding.rdbAdmin.isChecked();
        if (isAdmin && !email.contains(ADMIN_EMAIL_FORMAT)) {
            showToast(R.string.msg_email_invalid_admin);
            return;
        }
        
        if (!isAdmin && email.contains(ADMIN_EMAIL_FORMAT)) {
            showToast(R.string.msg_email_invalid_user);
            return;
        }
        
        signInUser(email, password);
    }

    private boolean validateInput(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            showToast(R.string.msg_email_require);
            binding.edtEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            showToast(R.string.msg_password_require);
            binding.edtPassword.requestFocus();
            return false;
        }

        if (!StringUtil.isValidEmail(email)) {
            showToast(R.string.msg_email_invalid);
            binding.edtEmail.requestFocus();
            return false;
        }

        return true;
    }

    private void signInUser(String email, String password) {
        showProgressDialog(true);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        handleSuccessfulSignIn(email);
                    } else {
                        handleSignInError(task.getException());
                        loginAttempts++;
                    }
                });
    }

    private void handleSuccessfulSignIn(String email) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            boolean isAdmin = email.contains(ADMIN_EMAIL_FORMAT);
            User userObject = new User(email);
            userObject.setAdmin(isAdmin);
            DataStoreManager.setUser(userObject);
            GlobalFunction.gotoMainActivity(this);
            finishAffinity();
        }
        DataStoreManager.setUser(userObject);
        GlobalFunction.gotoMainActivity(this);
        finishAffinity();
    }

    private void handleSignInError(Exception exception) {
        if (exception instanceof FirebaseAuthInvalidUserException) {
            showToast(R.string.msg_user_not_found);
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            showToast(R.string.msg_invalid_password);
        } else if (exception instanceof FirebaseTooManyRequestsException) {
            showToast(R.string.msg_too_many_requests);
        } else if (exception instanceof FirebaseNetworkException) {
            showToast(R.string.msg_network_error);
        } else {
            showToast(R.string.msg_sign_in_error);
        }
    }
}