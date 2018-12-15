package com.academy.fundamentals.mymovies.Screens.MovieDetails.MvpViews;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Screens.Common.MvpViews.ViewMvp;


public interface MovieDetailsFragmentView extends ViewMvp {

    interface MovieDetailsFragmentViewListener {
        void onTrailerClick();
    }

    void bindMovieDetails(Movie movie);

    void setListener(MovieDetailsFragmentViewListener listener);

    void unbindButterKnife();
}
