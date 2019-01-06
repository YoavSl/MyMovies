package com.academy.fundamentals.mymovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.R;
import com.academy.fundamentals.mymovies.Repositories.FavoritesRepository;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MoviesRecyclerAdapterViewHolder> {
    private final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private final MoviesAdapterOnClickHandler mClickHandler;
    private FavoritesRepository favoritesRepository;
    private List<Movie> movies = new ArrayList<>();
    private AlphaAnimation fadeIn, fadeOut;

    public interface MoviesAdapterOnClickHandler {
        void onMovieClick(int movieId);
    }

    private MoviesRecyclerAdapter(MoviesAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        favoritesRepository = FavoritesRepository.getInstance(context);

        confInFavoritesAnimations();
    }

    public MoviesRecyclerAdapter(MoviesAdapterOnClickHandler clickHandler, Context context, Movie movie) {
        this(clickHandler, context);
        this.movies.add(movie);
    }

    public MoviesRecyclerAdapter(MoviesAdapterOnClickHandler clickHandler, Context context, List<Movie> movies) {
        this(clickHandler, context);
        this.movies = movies;
    }

    @NonNull
    @Override
    public MoviesRecyclerAdapterViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        int layoutId = R.layout.recycler_item_movie;

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

    public void appendMovie(Movie movieToAppend) {
        movies.add(movieToAppend);
        notifyDataSetChanged();
    }

    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

    public void updateMoviesOrder(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    class MoviesRecyclerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {
        @BindView(R.id.posterIV) ImageView posterIV;
        @BindView(R.id.releaseDateCP) Chip releaseDateCP;
        @BindView(R.id.titleTV) TextView titleTV;
        @BindView(R.id.ratingTV) TextView ratingTV;
        @BindView(R.id.overviewTV) TextView overviewTV;
        @BindView(R.id.inFavoritesVW) View inFavoritesVW;

        private MoviesRecyclerAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        private void bind(final Movie movie) {
            releaseDateCP.setText(movie.getReleaseDate().split("-")[0]);   //Get only the year
            titleTV.setText(movie.getTitle());
            ratingTV.setText(String.valueOf(movie.getRating()));
            overviewTV.setText(movie.getOverview());

            Glide.with(itemView)
                    .load(IMAGE_BASE_URL + movie.getPosterPath())
                    .transition(withCrossFade())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(posterIV);

            if (favoritesRepository.checkIfExists(movie.getId()))
                inFavoritesVW.setVisibility(View.VISIBLE);
            else
                inFavoritesVW.setVisibility(View.INVISIBLE);

            /* Sort the issue of when the title consists of 2 lines or more, the overview
            text won't display ellipsize "end" at the end of line 3 (cause it's configured
            to 4 max lines) *//*
            titleTV.post(new Runnable() {
                @Override
                public void run() {
                    if (titleTV.getLineCount() == 2)
                        overviewTV.setMaxLines(3);
                }
            });*/
        }

        @Override
        public void onClick(View view) {
            int selectedMoviePos = getAdapterPosition();
            mClickHandler.onMovieClick(selectedMoviePos);
        }

        @Override
        public boolean onLongClick(View view) {
            int selectedMovieId = movies.get(getAdapterPosition()).getId();

            if (favoritesRepository.checkIfExists(selectedMovieId)) {
                /*movies.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());*/
                inFavoritesVW.startAnimation(fadeOut);
                favoritesRepository.removeItem(selectedMovieId);
            }
            else {
                inFavoritesVW.startAnimation(fadeIn);
                favoritesRepository.addItem(selectedMovieId);
            }
            return true;
        }
    }

    private void confInFavoritesAnimations(){
        fadeIn = new AlphaAnimation(0.0f , 1.0f );
        fadeIn.setDuration(400);
        fadeIn.setFillAfter(true);

        fadeOut = new AlphaAnimation( 1.0f , 0.0f );
        fadeOut.setDuration(400);
        fadeOut.setFillAfter(true);
    }
}