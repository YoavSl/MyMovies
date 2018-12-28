package com.academy.fundamentals.mymovies.Callbacks;

import com.academy.fundamentals.mymovies.Models.Review;

import java.util.List;


public interface OnGetReviewsCallback {
    void onSuccess(List<Review> reviews);

    void onError();
}