package com.academy.fundamentals.mymovies.Callbacks;

import com.academy.fundamentals.mymovies.Models.Trailer;

import java.util.List;


public interface OnGetTrailersCallback {
    void onSuccess(List<Trailer> trailers);

    void onError();
}
