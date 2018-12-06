package com.academy.fundamentals.mymovies.Screens.MoviesList.MvpViews;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.Adapters.MoviesRecyclerAdapter;
import com.academy.fundamentals.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MoviesListFragmentViewImpl implements MoviesListFragmentView,
        MoviesRecyclerAdapter.MoviesAdapterOnClickHandler {
    private static final String TAG = "MoviesListFtViewImpl";
    private final Context mContext;
    private View mRootView;
    private Unbinder unbinder;
    private MoviesListFragmentViewListener mListener;

    @BindView(R.id.moviesRV) RecyclerView moviesRV;

    public MoviesListFragmentViewImpl(LayoutInflater inflater, ViewGroup container) {
        mContext = inflater.getContext();
        mRootView = inflater.inflate(R.layout.fragment_movies_list, container, false);

        unbinder = ButterKnife.bind(this, mRootView);

        confMoviesList();
    }

    private void confMoviesList() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        moviesRV.setLayoutManager(layoutManager);
        moviesRV.setHasFixedSize(true);   //Use this setting to improve performance if you know that changes in content do not change the child layout size

        MoviesRecyclerAdapter mMoviesRecyclerAdapter = new MoviesRecyclerAdapter(this);
        moviesRV.setAdapter(mMoviesRecyclerAdapter);
    }

    @Override
    public void onClick(int movieIndex) {
        if (mListener != null) {
            mListener.onMovieClick(movieIndex);
        }
    }

    @Override
    public void setListener(MoviesListFragmentViewListener listener) {
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
