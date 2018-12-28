package com.academy.fundamentals.mymovies.Screens.FavoritesList.MvpViews;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.Adapters.MoviesRecyclerAdapter;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FavoritesListFragmentViewImpl implements FavoritesListFragmentView,
        MoviesRecyclerAdapter.MoviesAdapterOnClickHandler {
    private static final String TAG = "FavoritesListFtViewImpl";

    private View mRootView;
    private Unbinder unbinder;
    private FavoritesListFragmentViewListener mListener;
    private MoviesRecyclerAdapter mMoviesRecyclerAdapter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.moviesRV) RecyclerView moviesRV;

    public FavoritesListFragmentViewImpl(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_favorites_list, container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        confMoviesList();
    }

    private void confMoviesList() {
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(mRootView.getContext(), LinearLayoutManager.VERTICAL, false);

        moviesRV.setLayoutManager(layoutManager);
        moviesRV.setHasFixedSize(true);
    }

    @Override
    public void onClick(int selectedMoviePos) {
        if (mListener != null) {
            mListener.onMovieClick(selectedMoviePos);
        }
    }

    @Override
    public void setListener(FavoritesListFragmentViewListener listener) {
        mListener = listener;
    }

    @Override
    public void displayMovie(Movie movie) {
        if (mMoviesRecyclerAdapter == null) {
            mMoviesRecyclerAdapter = new MoviesRecyclerAdapter(mRootView.getContext(), this, movie);
            moviesRV.setAdapter(mMoviesRecyclerAdapter);
        }
        else
            mMoviesRecyclerAdapter.appendMovie(movie);
    }

    @Override
    public void displayEmptyList() {

    }

    @Override
    public void getMovieFailed(int movieId) {
        Log.e(TAG, "Error, couldn't get movie (id: " + movieId + ")");
    }

    @Override
    public void refreshList() {
        mMoviesRecyclerAdapter.notifyDataSetChanged();
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
