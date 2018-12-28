package com.academy.fundamentals.mymovies.Callbacks;

import com.academy.fundamentals.mymovies.Models.Genre;

import java.util.List;


public interface OnGetGenresCallback {

    void onSuccess(List<Genre> genres);

    void onError();
}
