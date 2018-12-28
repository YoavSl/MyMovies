package com.academy.fundamentals.mymovies.Callbacks;

import com.academy.fundamentals.mymovies.Models.Movie;


public interface OnGetMovieCallback {
    void onSuccess(Movie movie);

    void onError();
}
