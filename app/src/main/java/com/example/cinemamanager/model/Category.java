package com.example.cinemamanager.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Expose
    @SerializedName("id")
    private long id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("image")
    private String image;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("movie_count")
    private int movieCount;

    public Category() {
        // Required empty constructor for Firebase
    }

    public Category(long id, @NonNull String name, @NonNull String image) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = "";
        this.movieCount = 0;
    }

    public Category(long id, @NonNull String name, @NonNull String image,
                   @Nullable String description) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.movieCount = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = Math.max(0, id); // Ensure non-negative ID
    }

    @NonNull
    public String getName() {
        return name != null ? name : "";
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getImage() {
        return image != null ? image : "";
    }

    public void setImage(@NonNull String image) {
        this.image = image;
    }

    @NonNull
    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public int getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(int movieCount) {
        this.movieCount = Math.max(0, movieCount); // Ensure non-negative count
    }

    public void incrementMovieCount() {
        this.movieCount++;
    }

    public void decrementMovieCount() {
        if (this.movieCount > 0) {
            this.movieCount--;
        }
    }

    public boolean hasMovies() {
        return movieCount > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id &&
               movieCount == category.movieCount &&
               Objects.equals(name, category.name) &&
               Objects.equals(image, category.image) &&
               Objects.equals(description, category.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, image, description, movieCount);
    }

    @NonNull
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", movieCount=" + movieCount +
                '}';
    }
}
