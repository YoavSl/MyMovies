package com.academy.fundamentals.mymovies.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MoviesRecyclerAdapterViewHolder> {
    private final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private final MoviesAdapterOnClickHandler mClickHandler;
    private List<Movie> movies;

    public interface MoviesAdapterOnClickHandler {
        void onClick(int movieId);
    }

    public MoviesRecyclerAdapter(MoviesAdapterOnClickHandler clickHandler, List<Movie> movies) {
        mClickHandler = clickHandler;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MoviesRecyclerAdapterViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        int layoutId = R.layout.item_movie_result;

        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(layoutId, parent, false);

        return new MoviesRecyclerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesRecyclerAdapterViewHolder moviesRecyclerAdapterViewHolder, int position) {
        moviesRecyclerAdapterViewHolder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

    class MoviesRecyclerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.posterIV) ImageView posterIV;
        @BindView(R.id.releaseDateTV) TextView releaseDateTV;
        @BindView(R.id.ratingTV) TextView ratingTV;
        @BindView(R.id.titleTV) TextView titleTV;
        @BindView(R.id.overviewTV) TextView overviewTV;

        public MoviesRecyclerAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int selectedMoviePos = getAdapterPosition();
            mClickHandler.onClick(selectedMoviePos);
        }

        private void bind(Movie movie) {
            releaseDateTV.setText(movie.getReleaseDate().split("-")[0]);   //Get only the year
            ratingTV.setText(String.valueOf(movie.getRating()));
            titleTV.setText(movie.getTitle());
            overviewTV.setText(movie.getOverview());

            /*Glide.with(myFragment)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.loading_spinner)
                    .crossFade()
                    .into(myImageView);*/

            Glide.with(itemView)
                    .load(IMAGE_BASE_URL + movie.getPosterPath())
                    .transition(withCrossFade())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(posterIV);
        }
    }
}