package com.academy.fundamentals.mymovies.Screens.MoviesDetailsList.MvpViews;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.academy.fundamentals.mymovies.Adapters.MoviesDetailsPagerAdapter;
import com.academy.fundamentals.mymovies.Models.Genre;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MoviesDetailsListFragmentViewImpl implements MoviesDetailsListFragmentView {
    private static final String TAG = "MovDetailsLtFtViewImpl";

    private View mRootView;
    private Unbinder unbinder;
    private MoviesDetailsPagerAdapter mMoviesDetailsPagerAdapter;
    private Animation alphaAnimLoadingList, alphaAnimRemovingMovie;

    @BindView(R.id.moviesVP) ViewPager moviesVP;

    public MoviesDetailsListFragmentViewImpl(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_movies_details_list, container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        alphaAnimLoadingList = AnimationUtils.loadAnimation(inflater.getContext(), R.anim.anim_alpha_loading_movie_details_list_fragment);
        alphaAnimRemovingMovie = AnimationUtils.loadAnimation(inflater.getContext(), R.anim.anim_alpha_removing_movie_details_fragment);
    }

    @Override
    public void confMoviesList(List<Movie> movies, List<Genre> genres, int selectedMoviePos) {
        mMoviesDetailsPagerAdapter = new MoviesDetailsPagerAdapter(
                ((FragmentActivity) mRootView.getContext()).getSupportFragmentManager(),
                movies, genres);
        moviesVP.setAdapter(mMoviesDetailsPagerAdapter);
        moviesVP.setCurrentItem(selectedMoviePos);
        moviesVP.startAnimation(alphaAnimLoadingList);
    }

    @Override
    public void removeMovie(int pos) {
        moviesVP.startAnimation(alphaAnimRemovingMovie);
        mMoviesDetailsPagerAdapter.removeMovie(pos);
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