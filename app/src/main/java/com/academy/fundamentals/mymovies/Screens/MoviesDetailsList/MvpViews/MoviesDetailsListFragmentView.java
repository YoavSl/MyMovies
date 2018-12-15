package com.academy.fundamentals.mymovies.Screens.MoviesDetailsList.MvpViews;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Screens.Common.MvpViews.ViewMvp;

import java.util.List;


public interface MoviesDetailsListFragmentView extends ViewMvp {
    void confMoviesList(List<Movie> movies, int selectedMoviePos);

    void unbindButterKnife();
}
