package com.academy.fundamentals.mymovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class FavoriteMoviesRecyclerAdapter extends
        RecyclerView.Adapter<FavoriteMoviesRecyclerAdapter.FavoriteMoviesRecyclerAdapterViewHolder> {
    private final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private final FavoriteMoviesAdapterOnClickHandler mClickHandler;
    private Context mContext;
    private FavoritesRepository favoritesRepository;
    private List<Movie> movies;

    public interface FavoriteMoviesAdapterOnClickHandler {
        void onMovieClick(int selectedMoviePos);

        void onMovieLongClick(int selectedMoviePos);
    }

    public FavoriteMoviesRecyclerAdapter(FavoriteMoviesAdapterOnClickHandler clickHandler,
                                         Context context, List<Movie> movies) {
        mClickHandler = clickHandler;
        mContext = context;
        favoritesRepository = FavoritesRepository.getInstance(context);

        this.movies = new ArrayList<>(movies);
    }

    @NonNull
    @Override
    public FavoriteMoviesRecyclerAdapterViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        int layoutId = R.layout.recycler_item_movie;

        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(layoutId, parent, false);

        return new FavoriteMoviesRecyclerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull FavoriteMoviesRecyclerAdapterViewHolder favoriteMoviesRecyclerAdapterViewHolder,
            int position) {
        favoriteMoviesRecyclerAdapterViewHolder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void updateMoviesOrder(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void removeMovie(int pos) {
        movies.remove(pos);
        notifyItemRemoved(pos);
    }

    class FavoriteMoviesRecyclerAdapterViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.posterIV) ImageView posterIV;
        @BindView(R.id.releaseDateCP) Chip releaseDateCP;
        @BindView(R.id.titleTV) TextView titleTV;
        @BindView(R.id.ratingCG) Group ratingCG;
        @BindView(R.id.ratingTV) TextView ratingTV;
        @BindView(R.id.overviewTV) TextView overviewTV;
        @BindView(R.id.inFavoritesVW) View inFavoritesVW;

        private FavoriteMoviesRecyclerAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        private void bind(final Movie movie) {
            if (!favoritesRepository.checkIfExists(movie.getId())) {
                removeMovie(getAdapterPosition());
                return;
            }

            releaseDateCP.setText(movie.getReleaseDate().split("-")[0]);   //Get only the year
            titleTV.setText(movie.getTitle());

            if (movie.getReleaseDate().isEmpty())
                releaseDateCP.setVisibility(View.GONE);
            else
                releaseDateCP.setText(movie.getReleaseDate().split("-")[0]);   //Get only the year

            if (movie.getRating() == 0)   //Hasn't been rated yet
                ratingCG.setVisibility(View.GONE);
            else
                ratingTV.setText(String.valueOf(movie.getRating()));

            if (movie.getOverview().isEmpty())
                overviewTV.setText(mContext.getString(R.string.text_view_no_existing_overview));
            else
                overviewTV.setText(movie.getOverview());

            inFavoritesVW.setVisibility(View.VISIBLE);

            Glide.with(itemView)
                    .load(IMAGE_BASE_URL + movie.getPosterPath())
                    .transition(withCrossFade())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(posterIV);

            titleTV.post(new Runnable() {
                @Override
                public void run() {
                    if (titleTV.getLineCount() == 1)
                        overviewTV.setMaxLines(4);
                    else   //getLineCount() = 2
                        overviewTV.setMaxLines(3);
                }
            });
        }

        @Override
        public void onClick(View view) {
            int selectedMoviePos = getAdapterPosition();
            mClickHandler.onMovieClick(selectedMoviePos);
        }

        @Override
        public boolean onLongClick(View view) {
            int selectedMoviePos = getAdapterPosition();
            int currentMovieId = movies.get(selectedMoviePos).getId();

            removeMovie(selectedMoviePos);

            mClickHandler.onMovieLongClick(selectedMoviePos);
            favoritesRepository.removeItem(currentMovieId);

            return true;
        }
    }
}