package com.example.cinemamanager.fragment.admin;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cinemamanager.activity.ChangePasswordActivity;
import com.example.cinemamanager.activity.SignInActivity;
import com.example.cinemamanager.activity.admin.AdminRevenueActivity;
import com.example.cinemamanager.constant.GlobalFunction;
import com.example.cinemamanager.databinding.FragmentAdminManageBinding;
import com.example.cinemamanager.prefs.DataStoreManager;
import com.google.firebase.auth.FirebaseAuth;

public class AdminManageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAdminManageBinding fragmentAdminManageBinding = FragmentAdminManageBinding.inflate(inflater, container, false);

        fragmentAdminManageBinding.tvEmail.setText(DataStoreManager.getUser().getEmail());

        fragmentAdminManageBinding.layoutReport.setOnClickListener(v -> onClickReport());
        fragmentAdminManageBinding.layoutSignOut.setOnClickListener(v -> onClickSignOut());
        fragmentAdminManageBinding.layoutChangePassword.setOnClickListener(v -> onClickChangePassword());

        return fragmentAdminManageBinding.getRoot();
    }

    private void onClickReport() {
        GlobalFunction.startActivity(getActivity(), AdminRevenueActivity.class);
    }

    private void onClickChangePassword() {
        GlobalFunction.startActivity(getActivity(), ChangePasswordActivity.class);
    }

    private void onClickSignOut() {
        if (getActivity() == null) {
            return;
        }
        FirebaseAuth.getInstance().signOut();
        DataStoreManager.setUser(null);
        GlobalFunction.startActivity(getActivity(), SignInActivity.class);
        getActivity().finishAffinity();
    }
}
