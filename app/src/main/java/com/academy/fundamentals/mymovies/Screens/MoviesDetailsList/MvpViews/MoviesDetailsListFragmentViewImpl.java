package com.academy.fundamentals.mymovies.Screens.MoviesDetailsList.MvpViews;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.academy.fundamentals.mymovies.Adapters.MoviesDetailsPagerAdapter;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MoviesDetailsListFragmentViewImpl implements MoviesDetailsListFragmentView {
    private static final String TAG = "MovDetailsLtFtViewImpl";

    private View mRootView;
    private Unbinder unbinder;

    @BindView(R.id.moviesVP) ViewPager moviesVP;

    public MoviesDetailsListFragmentViewImpl(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_movies_details_list, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
    }

    @Override
    public void confMoviesList(List<Movie> movies, int selectedMoviePos) {
        MoviesDetailsPagerAdapter mMoviesDetailsPagerAdapter = new MoviesDetailsPagerAdapter(
                ((FragmentActivity) mRootView.getContext()).getSupportFragmentManager(),
                movies);
        moviesVP.setAdapter(mMoviesDetailsPagerAdapter);
        moviesVP.setCurrentItem(selectedMoviePos);
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
