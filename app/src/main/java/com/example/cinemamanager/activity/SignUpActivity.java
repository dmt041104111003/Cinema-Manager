package com.example.cinemamanager.activity;

import static com.example.cinemamanager.constant.ConstantKey.ADMIN_EMAIL_FORMAT;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.cinemamanager.R;
import com.example.cinemamanager.constant.GlobalFunction;
import com.example.cinemamanager.databinding.ActivitySignUpBinding;
import com.example.cinemamanager.model.User;
import com.example.cinemamanager.prefs.DataStoreManager;
import com.example.cinemamanager.util.StringUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends BaseActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeFirebase();
        setupDefaultUI();
        setupClickListeners();
    }

    private void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setupDefaultUI() {
        binding.rdbUser.setChecked(true);
    }

    private void setupClickListeners() {
        binding.imgBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSignIn.setOnClickListener(v -> finish());
        binding.btnSignUp.setOnClickListener(v -> onClickValidateSignUp());
    }

    private void onClickValidateSignUp() {
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

        signUpUser(email, password);
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

    private void signUpUser(String email, String password) {
        showProgressDialog(true);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        handleSuccessfulSignUp(email);
                    } else {
                        handleSignUpError(task.getException());
                    }
                });
    }

    private void handleSuccessfulSignUp(String email) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            boolean isAdmin = email.contains(ADMIN_EMAIL_FORMAT);
            User userObject = new User(email);
            userObject.setAdmin(isAdmin);
            DataStoreManager.setUser(userObject);
            GlobalFunction.gotoMainActivity(this);
            finishAffinity();
        }
    }

    private void handleSignUpError(@NonNull Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            showToast(R.string.msg_password_weak);
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            showToast(R.string.msg_email_invalid);
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            showToast(R.string.msg_email_exists);
        } else {
            showToast(R.string.msg_sign_up_error);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
    }
}