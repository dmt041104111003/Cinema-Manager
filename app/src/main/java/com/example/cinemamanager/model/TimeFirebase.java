package com.example.cinemamanager.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TimeFirebase implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("seats")
    private List<Seat> seats;

    public TimeFirebase() {
        // Required empty constructor for Firebase
        this.seats = new ArrayList<>();
    }

    public TimeFirebase(int id, @NonNull String title, @Nullable List<Seat> seats) {
        this.id = id;
        this.title = title;
        this.seats = seats != null ? seats : new ArrayList<>();
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

    @NonNull
    public List<Seat> getSeats() {
        return seats != null ? seats : new ArrayList<>();
    }

    public void setSeats(@Nullable List<Seat> seats) {
        this.seats = seats != null ? seats : new ArrayList<>();
    }

    public int getAvailableSeats() {
        int available = 0;
        for (Seat seat : getSeats()) {
            if (!seat.isBooked()) {
                available++;
            }
        }
        return available;
    }

    public boolean hasAvailableSeats() {
        return getAvailableSeats() > 0;
    }

    public boolean isValidTime() {
        try {
            TIME_FORMAT.parse(title);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isTimeAfter(@NonNull String otherTime) {
        try {
            Date thisTime = TIME_FORMAT.parse(title);
            Date other = TIME_FORMAT.parse(otherTime);
            return thisTime != null && other != null && thisTime.after(other);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeFirebase time = (TimeFirebase) o;
        return id == time.id &&
               Objects.equals(title, time.title) &&
               Objects.equals(seats, time.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, seats);
    }

    @NonNull
    @Override
    public String toString() {
        return "TimeFirebase{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", availableSeats=" + getAvailableSeats() +
                ", totalSeats=" + (seats != null ? seats.size() : 0) +
                '}';
    }
}
