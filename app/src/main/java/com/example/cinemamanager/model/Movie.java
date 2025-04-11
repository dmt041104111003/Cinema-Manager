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

public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    @Expose
    @SerializedName("id")
    private long id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("price")
    private int price;

    @Expose
    @SerializedName("date")
    private String date;

    @Expose
    @SerializedName("image")
    private String image;

    @Expose
    @SerializedName("image_banner")
    private String imageBanner;

    @Expose
    @SerializedName("url")
    private String url;

    @Expose
    @SerializedName("rooms")
    private List<RoomFirebase> rooms;

    @Expose
    @SerializedName("category_id")
    private long categoryId;

    @Expose
    @SerializedName("category_name")
    private String categoryName;

    @Expose
    @SerializedName("booked")
    private int booked;

    public Movie() {
        // Required empty constructor for Firebase
        this.rooms = new ArrayList<>();
    }

    public Movie(long id, @NonNull String name, @NonNull String description, int price,
                @NonNull String date, @NonNull String image, @NonNull String imageBanner,
                @NonNull String url, @Nullable List<RoomFirebase> rooms,
                long categoryId, @NonNull String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = Math.max(0, price); // Ensure non-negative price
        this.date = date;
        this.image = image;
        this.imageBanner = imageBanner;
        this.url = url;
        this.rooms = rooms != null ? rooms : new ArrayList<>();
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.booked = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name != null ? name : "";
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = Math.max(0, price); // Ensure non-negative price
    }

    @NonNull
    public String getDate() {
        return date != null ? date : "";
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public String getImage() {
        return image != null ? image : "";
    }

    public void setImage(@NonNull String image) {
        this.image = image;
    }

    @NonNull
    public String getImageBanner() {
        return imageBanner != null ? imageBanner : "";
    }

    public void setImageBanner(@NonNull String imageBanner) {
        this.imageBanner = imageBanner;
    }

    @NonNull
    public String getUrl() {
        return url != null ? url : "";
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    @NonNull
    public List<RoomFirebase> getRooms() {
        return rooms != null ? rooms : new ArrayList<>();
    }

    public void setRooms(@Nullable List<RoomFirebase> rooms) {
        this.rooms = rooms != null ? rooms : new ArrayList<>();
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @NonNull
    public String getCategoryName() {
        return categoryName != null ? categoryName : "";
    }

    public void setCategoryName(@NonNull String categoryName) {
        this.categoryName = categoryName;
    }

    public int getBooked() {
        return booked;
    }

    public void setBooked(int booked) {
        this.booked = Math.max(0, booked); // Ensure non-negative bookings
    }

    public boolean isAvailable() {
        try {
            Date movieDate = DATE_FORMAT.parse(date);
            return movieDate != null && movieDate.after(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasAvailableSeats() {
        int totalSeats = 0;
        for (RoomFirebase room : getRooms()) {
            for (TimeFirebase time : room.getTimes()) {
                totalSeats += time.getSeats().size();
            }
        }
        return booked < totalSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id &&
               price == movie.price &&
               categoryId == movie.categoryId &&
               booked == movie.booked &&
               Objects.equals(name, movie.name) &&
               Objects.equals(description, movie.description) &&
               Objects.equals(date, movie.date) &&
               Objects.equals(image, movie.image) &&
               Objects.equals(imageBanner, movie.imageBanner) &&
               Objects.equals(url, movie.url) &&
               Objects.equals(rooms, movie.rooms) &&
               Objects.equals(categoryName, movie.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, date, image,
                imageBanner, url, rooms, categoryId, categoryName, booked);
    }

    @NonNull
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", date='" + date + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", booked=" + booked +
                '}';
    }
}
