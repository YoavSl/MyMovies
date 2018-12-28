package com.academy.fundamentals.mymovies.Screens.FavoritesList.MvpViews;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Screens.Common.MvpViews.ViewMvp;

import java.util.List;


public interface FavoritesListFragmentView extends ViewMvp {

    interface FavoritesListFragmentViewListener {
        void onMovieClick(int selectedMoviePos);
    }
    void setListener(FavoritesListFragmentViewListener listener);

    void displayMovie(Movie movie);

    void displayEmptyList();

    void getMovieFailed(int id);

    void refreshList();

    void unbindButterKnife();
}
