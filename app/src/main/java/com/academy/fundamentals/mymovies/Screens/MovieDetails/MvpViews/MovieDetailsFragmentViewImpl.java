package com.academy.fundamentals.mymovies.Screens.MovieDetails.MvpViews;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class MovieDetailsFragmentViewImpl implements MovieDetailsFragmentView {
    private static final String TAG = "MovieDetailsFtViewImpl";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static final String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    private static final String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";

    private View mRootView;
    private Unbinder unbinder;
    private MovieDetailsFragmentViewListener mListener;

    @BindView(R.id.movieBackdropIV) ImageView movieBackdropIV;
    @BindView(R.id.movieTitleTV) TextView movieTitleTV;
    @BindView(R.id.movieOverviewTV) TextView movieOverviewTV;
    @BindView(R.id.trailerBT) FloatingActionButton trailerBT;

    public MovieDetailsFragmentViewImpl(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
    }

    @OnClick(R.id.trailerBT)
    void OnClick(View view) {
        if (mListener != null) {
            mListener.onTrailerClick();
        }
    }

    @Override
    public void bindMovieDetails(Movie movie) {
        movieTitleTV.setText(movie.getTitle());
        movieOverviewTV.setText(movie.getOverview());

        Glide.with(mRootView.getContext())
                .load(IMAGE_BASE_URL + movie.getBackdropPath())
                .transition(withCrossFade())
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(movieBackdropIV);
    }

    @Override
    public void setListener(MovieDetailsFragmentViewListener listener) {
        mListener = listener;
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    @Override
    public void unbindButterKnife() {
        unbinder.unbind();
    }
}
