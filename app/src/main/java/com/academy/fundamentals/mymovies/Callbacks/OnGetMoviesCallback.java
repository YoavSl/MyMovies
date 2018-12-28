package com.academy.fundamentals.mymovies.Callbacks;

import com.academy.fundamentals.mymovies.Models.Movie;

import java.util.List;


public interface OnGetMoviesCallback {
    void onSuccess(List<Movie> movies, int page);

    void onError();
}
