package com.academy.fundamentals.mymovies.Screens.MovieDetails.MvpViews;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.academy.fundamentals.mymovies.Adapters.ReviewsRecyclerAdapter;
import com.academy.fundamentals.mymovies.Models.Genre;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Models.Review;
import com.academy.fundamentals.mymovies.Models.Trailer;
import com.academy.fundamentals.mymovies.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class MovieDetailsFragmentViewImpl implements MovieDetailsFragmentView {
    private static final String TAG = "MovieDetailsFtViewImpl";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static final String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    private static final String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";

    private LayoutInflater mInflater;
    private View mRootView;
    private Unbinder unbinder;
    private MovieDetailsFragmentViewListener mListener;

    @BindView(R.id.movieBackdropIV) ImageView movieBackdropIV;
    @BindView(R.id.movieTitleTB) Toolbar movieTitleTB;
    @BindView(R.id.favoriteFAB) FloatingActionButton favoriteFAB;
    @BindView(R.id.detailsCL) ConstraintLayout detailsCL;
    @BindView(R.id.movieRatingRB) RatingBar movieRatingRB;
    @BindView(R.id.movieReleaseDateGenresCG) ChipGroup movieReleaseDateGenresCG;
    @BindView(R.id.movieOverviewTV) TextView movieOverviewTV;
    @BindView(R.id.movieTrailersLL) LinearLayout movieTrailersLL;
    @BindView(R.id.noTrailersTV) TextView noTrailersTV;
    @BindView(R.id.reviewsRV) RecyclerView reviewsRV;
    @BindView(R.id.noReviewsTV) TextView noReviewsTV;

    public MovieDetailsFragmentViewImpl(LayoutInflater inflater, ViewGroup container) {
       mInflater = inflater;
       mRootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

       unbinder = ButterKnife.bind(this, mRootView);
    }

    @OnClick(R.id.favoriteFAB)
    void OnClick(View view) {
        if (mListener != null) {
            mListener.onFavoriteFabClick();
        }
    }

    @Override
    public void bindMovieDetails(Movie movie, List<Genre> genres, boolean inFavorites) {
        movieTitleTB.setTitle(movie.getTitle());
        movieRatingRB.setRating(movie.getRating());

        if (!movie.getReleaseDate().isEmpty()) {
            Chip releaseDateChip = (Chip) mInflater.inflate(R.layout.chip_group_item_release_date, movieReleaseDateGenresCG, false);
            releaseDateChip.setText(movie.getReleaseDate().split("-")[0]);   //Get only the year
            movieReleaseDateGenresCG.addView(releaseDateChip);
        }

        if (movie.getOverview().isEmpty())
            movieOverviewTV.setText(mRootView.getContext().
                    getString(R.string.text_view_no_existing_overview));
        else
            movieOverviewTV.setText(movie.getOverview());

        setMovieGenres(genres, movie.getGenreIds());
        setFavoriteFab(inFavorites);

        Glide.with(mRootView.getContext())
                .load(IMAGE_BASE_URL + movie.getBackdropPath())
                .transition(withCrossFade())
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(movieBackdropIV);
    }

    private void setMovieGenres(List<Genre> genres, List<Integer> genreIds) {
        for (Integer genreId : genreIds) {
            for (Genre genre : genres) {
                if (genre.getId() == genreId) {
                    Chip genreChip = (Chip) mInflater.inflate(R.layout.chip_group_item_genre, movieReleaseDateGenresCG, false);
                    genreChip.setText(genre.getName());
                    movieReleaseDateGenresCG.addView(genreChip);
                    break;
                }
            }
        }
    }

    @Override
    public void setFavoriteFab(boolean inFavorites) {
        if (inFavorites)
            favoriteFAB.setImageResource(R.drawable.ic_in_favorites);
        else
            favoriteFAB.setImageResource(R.drawable.ic_favorite);
    }

    @Override
    public void displayTrailers(List<Trailer> trailers) {
        if (trailers.size() > 0) {
            for (final Trailer trailer : trailers) {
                View parent = mInflater.inflate(R.layout.thumbnail_trailer, movieTrailersLL, false);
                ImageView thumbnail = parent.findViewById(R.id.thumbnailIV);

                thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null)
                            mListener.onTrailerClick(String.format(YOUTUBE_VIDEO_URL, trailer.getKey()));
                    }
                });

                Glide.with(mRootView)
                        .load(String.format(YOUTUBE_THUMBNAIL_URL, trailer.getKey()))
                        .apply(RequestOptions.placeholderOf(R.color.colorPrimary).centerCrop())
                        .into(thumbnail);

                movieTrailersLL.addView(parent);
            }
        }
        else {
            noTrailersTV.setVisibility(View.VISIBLE);
            noTrailersTV.setText(R.string.text_view_no_existing_trailers);

            ConstraintSet set = new ConstraintSet();
            set.clone(detailsCL);
            set.connect(R.id.reviewsLabelTV, ConstraintSet.TOP, R.id.noTrailersTV, ConstraintSet.BOTTOM);
            set.applyTo(detailsCL);
        }
    }

    @Override
    public void displayReviews(List<Review> reviews) {
        if (reviews.isEmpty()) {
            noReviewsTV.setVisibility(View.VISIBLE);
            noReviewsTV.setText(R.string.text_view_no_existing_reviews);
        }
        else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(mRootView.getContext(),
                    LinearLayoutManager.VERTICAL, false);
            reviewsRV.setLayoutManager(layoutManager);

            ReviewsRecyclerAdapter mReviewsRecyclerAdapter = new ReviewsRecyclerAdapter(reviews);
            reviewsRV.setAdapter(mReviewsRecyclerAdapter);
        }
    }

    @Override
    public void getTrailersFailed() {
        noTrailersTV.setVisibility(View.VISIBLE);
        noTrailersTV.setText(R.string.text_view_get_trailers_failed);

        ConstraintSet set = new ConstraintSet();
        set.clone(detailsCL);
        set.connect(R.id.reviewsLabelTV, ConstraintSet.TOP, R.id.noTrailersTV, ConstraintSet.BOTTOM);
        set.applyTo(detailsCL);
    }

    @Override
    public void getReviewsFailed() {
        noReviewsTV.setVisibility(View.VISIBLE);
        noReviewsTV.setText(R.string.text_view_get_reviews_failed);
    }

    @Override
    public void setListener(MovieDetailsFragmentViewListener listener) {
        mListener = listener;
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    @Override
    public void unbindButterKnife() {
        unbinder.unbind();
    }
}