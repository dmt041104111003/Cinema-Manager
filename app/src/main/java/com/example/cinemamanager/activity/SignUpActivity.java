package com.example.cinemamanager.activity;

import static com.example.cinemamanager.constant.ConstantKey.ADMIN_EMAIL_FORMAT;

import android.os.Bundle;
import android.widget.Toast;

import com.example.cinemamanager.R;
import com.example.cinemamanager.constant.GlobalFunction;
import com.example.cinemamanager.databinding.ActivitySignUpBinding;
import com.example.cinemamanager.model.User;
import com.example.cinemamanager.prefs.DataStoreManager;
import com.example.cinemamanager.util.StringUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends BaseActivity {

    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rdbUser.setChecked(true);
        initListener();
    }

    private void initListener() {
        binding.imgBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSignIn.setOnClickListener(v -> finish());
        binding.btnSignUp.setOnClickListener(v -> onClickValidateSignUp());
    }

    private void onClickValidateSignUp() {
        String strEmail = binding.edtEmail.getText().toString().trim();
        String strPassword = binding.edtPassword.getText().toString().trim();
        
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(this, R.string.msg_email_require, Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(this, R.string.msg_password_require, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(this, R.string.msg_email_invalid, Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.rdbAdmin.isChecked()) {
            if (!strEmail.contains(ADMIN_EMAIL_FORMAT)) {
                Toast.makeText(this, R.string.msg_email_invalid_admin, Toast.LENGTH_SHORT).show();
                return;
            }
            signUpUser(strEmail, strPassword);
            return;
        }

        if (strEmail.contains(ADMIN_EMAIL_FORMAT)) {
            Toast.makeText(this, R.string.msg_email_invalid_user, Toast.LENGTH_SHORT).show();
            return;
        }
        
        signUpUser(strEmail, strPassword);
    }

    private void signUpUser(String email, String password) {
        showProgressDialog(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
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
                        Toast.makeText(this, R.string.msg_sign_up_error,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
