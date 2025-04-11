package com.example.cinemamanager.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("selected")
    private boolean selected;

    @Expose
    @SerializedName("booked")
    private boolean booked;

    @Expose
    @SerializedName("user_id")
    private String userId;

    public Seat() {
        // Required empty constructor for Firebase
    }

    public Seat(int id, @NonNull String title) {
        this.id = id;
        this.title = title;
        this.selected = false;
        this.booked = false;
        this.userId = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = Math.max(0, id); // Ensure non-negative ID
    }

    @NonNull
    public String getTitle() {
        return title != null ? title : "";
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
        if (!booked) {
            this.userId = null;
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        this.booked = userId != null;
    }

    public boolean isAvailable() {
        return !booked && !selected;
    }

    public boolean isBookedByUser(String userId) {
        return booked && Objects.equals(this.userId, userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return id == seat.id &&
               selected == seat.selected &&
               booked == seat.booked &&
               Objects.equals(title, seat.title) &&
               Objects.equals(userId, seat.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, selected, booked, userId);
    }

    @NonNull
    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", selected=" + selected +
                ", booked=" + booked +
                ", userId='" + userId + '\'' +
                '}';
    }
}
