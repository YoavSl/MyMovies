package com.academy.fundamentals.mymovies.Screens.FavoritesList.MvpViews;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Models.SortType;
import com.academy.fundamentals.mymovies.Screens.Common.MvpViews.ViewMvp;

import java.util.List;


public interface FavoritesListFragmentView extends ViewMvp {

    interface FavoritesListFragmentViewListener {
        void onSortModeClick(SortType sortType, boolean ascendingSort);

        void onMovieClick(int selectedMoviePos);

        void onMovieLongClick(int selectedMoviePos);
    }

    void setListener(FavoritesListFragmentViewListener listener);

    void setSortMenu();

    void displayLoadingAnimation();

    void displayMovies(List<Movie> movies);

    void updateMoviesOrder(List<Movie> movies);

    void removeMovie(int pos);

    void displayMoviesCount(int moviesCount);

    void displayEmptyList(boolean error);

    void getMoviesFailed(boolean failedEntirely);

    void unbindButterKnife();
}
