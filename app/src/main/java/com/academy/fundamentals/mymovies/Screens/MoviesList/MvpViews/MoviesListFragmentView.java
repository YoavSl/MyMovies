package com.academy.fundamentals.mymovies.Screens.MoviesList.MvpViews;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Screens.Common.MvpViews.ViewMvp;

import java.util.List;


public interface MoviesListFragmentView extends ViewMvp {

    interface MoviesListFragmentViewListener {
        void onCategoriesListClick();

        void onFavoritesListClick();

        void onMovieClick(int selectedMoviePos);

        void onListScroll();
    }

    void setListener(MoviesListFragmentViewListener listener);

    void setToolbarTitle(String category);

    void displayMovies(List<Movie> movies);

    void getMoviesFailed();

    void refreshList();

    void unbindButterKnife();
}
