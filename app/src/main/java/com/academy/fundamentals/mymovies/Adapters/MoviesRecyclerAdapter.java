package com.academy.fundamentals.mymovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
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


public class MoviesRecyclerAdapter extends
        RecyclerView.Adapter<MoviesRecyclerAdapter.MoviesRecyclerAdapterViewHolder> {
    private final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private final MoviesAdapterOnClickHandler mClickHandler;
    private Context mContext;
    private FavoritesRepository favoritesRepository;
    private List<Movie> movies;
    private AlphaAnimation fadeInAnim, fadeOutAnim;

    public interface MoviesAdapterOnClickHandler {
        void onMovieClick(int selectedMoviePos);
    }

    public MoviesRecyclerAdapter(MoviesAdapterOnClickHandler clickHandler, Context context, List<Movie> movies) {
        mClickHandler = clickHandler;
        mContext = context;
        favoritesRepository = FavoritesRepository.getInstance(context);

        this.movies = new ArrayList<>(movies);

        confInFavoritesAnimations();
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

    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

    class MoviesRecyclerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {
        @BindView(R.id.posterIV) ImageView posterIV;
        @BindView(R.id.releaseDateCP) Chip releaseDateCP;
        @BindView(R.id.titleTV) TextView titleTV;
        @BindView(R.id.ratingCG) Group ratingCG;
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

            Glide.with(itemView)
                    .load(IMAGE_BASE_URL + movie.getPosterPath())
                    .transition(withCrossFade())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(posterIV);

            if (favoritesRepository.checkIfExists(movie.getId()))
                inFavoritesVW.setVisibility(View.VISIBLE);
            else
                inFavoritesVW.setVisibility(View.INVISIBLE);

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
            int currentMovieId = movies.get(getAdapterPosition()).getId();

            if (favoritesRepository.checkIfExists(currentMovieId)) {
                inFavoritesVW.startAnimation(fadeOutAnim);
                favoritesRepository.removeItem(currentMovieId);
            }
            else {
                inFavoritesVW.startAnimation(fadeInAnim);
                favoritesRepository.addItem(currentMovieId);
            }

            return true;
        }
    }

    private void confInFavoritesAnimations(){
        fadeInAnim = new AlphaAnimation(0.0f , 1.0f );
        fadeInAnim.setDuration(mContext.getResources().
                getInteger(R.integer.animation_duration_view_in_favorites));
        fadeInAnim.setFillAfter(true);

        fadeOutAnim = new AlphaAnimation( 1.0f , 0.0f );
        fadeOutAnim.setDuration(mContext.getResources().
                getInteger(R.integer.animation_duration_view_in_favorites));
        fadeOutAnim.setFillAfter(true);
    }
}