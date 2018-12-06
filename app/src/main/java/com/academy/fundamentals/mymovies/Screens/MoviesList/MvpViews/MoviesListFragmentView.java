package com.academy.fundamentals.mymovies.Screens.MoviesList.MvpViews;

import com.academy.fundamentals.mymovies.Screens.Common.MvpViews.ViewMvp;


public interface MoviesListFragmentView extends ViewMvp {

    interface MoviesListFragmentViewListener {
        void onMovieClick(int movieIndex);
    }

    void setListener(MoviesListFragmentViewListener listener);

    void unbindButterKnife();
}
