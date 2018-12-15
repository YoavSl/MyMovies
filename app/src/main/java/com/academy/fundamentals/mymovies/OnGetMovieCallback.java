package com.academy.fundamentals.mymovies;

import com.academy.fundamentals.mymovies.Models.Movie;


public interface OnGetMovieCallback {
    void onSuccess(Movie movie);

    void onError();
}
