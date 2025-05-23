package com.example.cinemamanager.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemamanager.databinding.ItemMovieBinding;
import com.example.cinemamanager.model.Movie;
import com.example.cinemamanager.util.GlideUtils;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<Movie> ListMovies;
    private final IManagerMovieListener iManagerMovieListener;

    public interface IManagerMovieListener {
        void clickItemMovie(Movie movie);
    }

    public MovieAdapter(List<Movie> ListMovies, IManagerMovieListener iManagerMovieListener) {
        this.ListMovies = ListMovies;
        this.iManagerMovieListener = iManagerMovieListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding itemMovieBinding = ItemMovieBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieViewHolder(itemMovieBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = ListMovies.get(position);
        if (movie == null) {
            return;
        }
        GlideUtils.loadUrl(movie.getImage(), holder.mItemMovieBinding.imgMovie);
        holder.mItemMovieBinding.tvName.setText(movie.getName());
        holder.mItemMovieBinding.tvBooked.setText(String.valueOf(movie.getBooked()));

        holder.mItemMovieBinding.layoutItem.setOnClickListener(v -> iManagerMovieListener.clickItemMovie(movie));
    }

    @Override
    public int getItemCount() {
        if (ListMovies != null) {
            return ListMovies.size();
        }
        return 0;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        private final ItemMovieBinding mItemMovieBinding;

        public MovieViewHolder(@NonNull ItemMovieBinding itemMovieBinding) {
            super(itemMovieBinding.getRoot());
            this.mItemMovieBinding = itemMovieBinding;
        }
    }
}
