package com.example.cinemamanager.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Gson GSON = new Gson();

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("is_admin")
    private boolean isAdmin;

    @Expose
    @SerializedName("display_name")
    private String displayName;

    @Expose
    @SerializedName("phone_number")
    private String phoneNumber;

    public User() {
        // Required empty constructor for Firebase
    }

    public User(@NonNull String email) {
        this.email = email;
        this.displayName = email.split("@")[0];
    }

    @NonNull
    public String getEmail() {
        return email != null ? email : "";
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
        if (displayName == null || displayName.isEmpty()) {
            this.displayName = email.split("@")[0];
        }
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @NonNull
    public String getDisplayName() {
        return displayName != null ? displayName : getEmail().split("@")[0];
    }

    public void setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
    }

    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Nullable String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NonNull
    public String toJson() {
        return GSON.toJson(this);
    }

    @NonNull
    public static User fromJson(@NonNull String json) {
        return GSON.fromJson(json, User.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isAdmin == user.isAdmin &&
               Objects.equals(email, user.email) &&
               Objects.equals(displayName, user.displayName) &&
               Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, isAdmin, displayName, phoneNumber);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", displayName='" + displayName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
