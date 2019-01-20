package com.academy.fundamentals.mymovies.Screens.MovieDetails.MvpViews;

import com.academy.fundamentals.mymovies.Models.Genre;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Models.Review;
import com.academy.fundamentals.mymovies.Models.Trailer;
import com.academy.fundamentals.mymovies.Screens.Common.MvpViews.ViewMvp;

import java.util.List;


public interface MovieDetailsFragmentView extends ViewMvp {

    interface MovieDetailsFragmentViewListener {
        void onTrailerClick(String url);

        void onFavoriteFabClick();
    }

    void bindMovieDetails(Movie movie, List<Genre> genres, boolean inFavorites);

    void setFavoriteFab(boolean inFavorites);

    void displayTrailers(List<Trailer> trailers);

    void displayReviews(List<Review> reviews);

    void getTrailersFailed();

    void getReviewsFailed();

    void setListener(MovieDetailsFragmentViewListener listener);

    void unbindButterKnife();
}
