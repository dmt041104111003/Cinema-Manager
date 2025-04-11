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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseNetworkException;

public class SignInActivity extends BaseActivity {

    private ActivitySignInBinding mActivitySignInBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySignInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(mActivitySignInBinding.getRoot());

        mActivitySignInBinding.rdbUser.setChecked(true);

        mActivitySignInBinding.layoutSignUp.setOnClickListener(
                v -> GlobalFunction.startActivity(SignInActivity.this, SignUpActivity.class));

        mActivitySignInBinding.btnSignIn.setOnClickListener(v -> onClickValidateSignIn());
        mActivitySignInBinding.tvForgotPassword.setOnClickListener(v -> onClickForgotPassword());
    }

    private void onClickForgotPassword() {
        GlobalFunction.startActivity(this, ForgotPasswordActivity.class);
    }

    private void onClickValidateSignIn() {
        String strEmail = mActivitySignInBinding.edtEmail.getText().toString().trim();
        String strPassword = mActivitySignInBinding.edtPassword.getText().toString().trim();
        
        if (!validateInputs(strEmail, strPassword)) {
            return;
        }

        boolean isAdmin = mActivitySignInBinding.rdbAdmin.isChecked();
        if (isAdmin && !strEmail.contains(ADMIN_EMAIL_FORMAT)) {
            showToast(R.string.msg_email_invalid_admin);
            return;
        }
        
        if (!isAdmin && strEmail.contains(ADMIN_EMAIL_FORMAT)) {
            showToast(R.string.msg_email_invalid_user);
            return;
        }
        
        signInUser(strEmail, strPassword);
    }

    private boolean validateInputs(String email, String password) {
        if (StringUtil.isEmpty(email)) {
            showToast(R.string.msg_email_require);
            return false;
        }
        if (StringUtil.isEmpty(password)) {
            showToast(R.string.msg_password_require);
            return false;
        }
        if (!StringUtil.isValidEmail(email)) {
            showToast(R.string.msg_email_invalid);
            return false;
        }
        return true;
    }

    private void showToast(@StringRes int messageId) {
        Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show();
    }

    private void signInUser(String email, String password) {
        showProgressDialog(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        handleSuccessfulSignIn(firebaseAuth.getCurrentUser());
                    } else {
                        handleSignInError(task.getException());
                    }
                });
    }

    private void handleSuccessfulSignIn(FirebaseUser user) {
        if (user == null) {
            showToast(R.string.msg_sign_in_error);
            return;
        }

        String email = user.getEmail();
        if (email == null) {
            showToast(R.string.msg_sign_in_error);
            return;
        }

        User userObject = new User(email);
        if (email.contains(ADMIN_EMAIL_FORMAT)) {
            userObject.setAdmin(true);
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