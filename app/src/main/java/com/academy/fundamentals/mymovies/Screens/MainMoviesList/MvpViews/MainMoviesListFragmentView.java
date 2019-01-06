package com.academy.fundamentals.mymovies.Screens.MainMoviesList.MvpViews;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Models.SortType;
import com.academy.fundamentals.mymovies.Screens.Common.MvpViews.ViewMvp;

import java.util.List;


public interface MainMoviesListFragmentView extends ViewMvp {

    interface MainMoviesListFragmentViewListener {
        void onFavoritesListClick();

        void onMovieClick(int selectedMoviePos);

        void onListScroll();
    }

    void setListener(MainMoviesListFragmentViewListener listener);

    void displayMovies(List<Movie> movies);

    void getMoviesFailed();

    void refreshList();

    void unbindButterKnife();
}
