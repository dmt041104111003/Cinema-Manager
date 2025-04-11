package com.example.cinemamanager.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoomFirebase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("times")
    private List<TimeFirebase> times;

    public RoomFirebase() {
        // Required empty constructor for Firebase
        this.times = new ArrayList<>();
    }

    public RoomFirebase(int id, @NonNull String title, @Nullable List<TimeFirebase> times) {
        this.id = id;
        this.title = title;
        this.times = times != null ? times : new ArrayList<>();
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
    public List<TimeFirebase> getTimes() {
        return times != null ? times : new ArrayList<>();
    }

    public void setTimes(@Nullable List<TimeFirebase> times) {
        this.times = times != null ? times : new ArrayList<>();
    }

    public int getAvailableSeats() {
        int totalSeats = 0;
        for (TimeFirebase time : getTimes()) {
            totalSeats += time.getAvailableSeats();
        }
        return totalSeats;
    }

    public boolean hasAvailableSeats() {
        return getAvailableSeats() > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomFirebase room = (RoomFirebase) o;
        return id == room.id &&
               Objects.equals(title, room.title) &&
               Objects.equals(times, room.times);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, times);
    }

    @NonNull
    @Override
    public String toString() {
        return "RoomFirebase{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", timesCount=" + (times != null ? times.size() : 0) +
                '}';
    }
}
