package com.example.cinemamanager.activity;

import static com.example.cinemamanager.constant.ConstantKey.ADMIN_EMAIL_FORMAT;

import android.os.Bundle;
import android.widget.Toast;

import com.example.cinemamanager.R;
import com.example.cinemamanager.constant.GlobalFunction;
import com.example.cinemamanager.databinding.ActivitySignInBinding;
import com.example.cinemamanager.model.User;
import com.example.cinemamanager.prefs.DataStoreManager;
import com.example.cinemamanager.util.StringUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends BaseActivity {

    private static final String TAG = SignInActivity.class.getSimpleName();
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final long LOCKOUT_DURATION_MS = 300000; // 5 minutes

    private ActivitySignInBinding mActivitySignInBinding;
    private FirebaseAuth mFirebaseAuth;
    private int loginAttempts;
    private long lastLoginAttemptTime;
    private boolean isLoggingIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViews();
        initializeFirebase();
        setupClickListeners();
        checkPreviousSession();
    }

    private void initializeViews() {
        mActivitySignInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(mActivitySignInBinding.getRoot());
    }

    private void initializeFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        loginAttempts = 0;
        lastLoginAttemptTime = 0;
    }

    private void setupClickListeners() {
        mActivitySignInBinding.layoutSignUp.setOnClickListener(v -> goToSignUp());
        mActivitySignInBinding.btnSignIn.setOnClickListener(v -> onClickSignIn());
        mActivitySignInBinding.tvForgotPassword.setOnClickListener(v -> onClickForgotPassword());
    }

    private void checkPreviousSession() {
        if (mFirebaseAuth.getCurrentUser() != null && DataStoreManager.getUserId() != null) {
            goToMainActivity();
            return;
        }
    }

    private void goToSignUp() {
        GlobalFunction.startActivity(this, SignUpActivity.class);
    }

    private void onClickForgotPassword() {
        GlobalFunction.startActivity(this, ForgotPasswordActivity.class);
    }

    private void onClickSignIn() {
        if (isLoggingIn) {
            showMessage(getString(R.string.msg_login_in_progress));
            return;
        }

        if (isLockedOut()) {
            long remainingTime = LOCKOUT_DURATION_MS - (System.currentTimeMillis() - lastLoginAttemptTime);
            showErrorDialog(getString(R.string.msg_account_locked, remainingTime / 60000));
            return;
        }

        String strEmail = mActivitySignInBinding.edtEmail.getText().toString().trim();
        String strPassword = mActivitySignInBinding.edtPassword.getText().toString().trim();

        if (!validateInput(strEmail, strPassword)) {
            return;
        }

        signIn(strEmail, strPassword);
    }

    private boolean validateInput(String email, String password) {
        if (StringUtil.isEmpty(email)) {
            showErrorDialog(getString(R.string.msg_email_require));
            mActivitySignInBinding.edtEmail.requestFocus();
            return false;
        }

        if (!StringUtil.isValidEmail(email)) {
            showErrorDialog(getString(R.string.msg_email_invalid));
            mActivitySignInBinding.edtEmail.requestFocus();
            return false;
        }

        if (StringUtil.isEmpty(password)) {
            showErrorDialog(getString(R.string.msg_password_require));
            mActivitySignInBinding.edtPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            showErrorDialog(getString(R.string.msg_password_length));
            mActivitySignInBinding.edtPassword.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isLockedOut() {
        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            long elapsedTime = System.currentTimeMillis() - lastLoginAttemptTime;
            return elapsedTime < LOCKOUT_DURATION_MS;
        }
        return false;
    }

    private void signIn(String email, String password) {
        isLoggingIn = true;
        showProgressDialog(true);

        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    isLoggingIn = false;
                    showProgressDialog(false);

                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            User userObject = new User(user.getEmail(), password);
                            if (user.getEmail() != null && user.getEmail().contains(ADMIN_EMAIL_FORMAT)) {
                                userObject.setAdmin(true);
                            }
                            DataStoreManager.setUser(userObject);
                            GlobalFunction.gotoMainActivity(this);
                            finishAffinity();
                        }
                    } else {
                        Toast.makeText(SignInActivity.this, getString(R.string.msg_sign_in_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}