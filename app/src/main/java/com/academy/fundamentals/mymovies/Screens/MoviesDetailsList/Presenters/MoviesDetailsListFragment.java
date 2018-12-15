package com.academy.fundamentals.mymovies.Screens.MoviesDetailsList.Presenters;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.Screens.Common.Presenters.BaseFragment;
import com.academy.fundamentals.mymovies.Screens.MoviesDetailsList.MvpViews.MoviesDetailsListFragmentView;
import com.academy.fundamentals.mymovies.Screens.MoviesDetailsList.MvpViews.MoviesDetailsListFragmentViewImpl;


public class MoviesDetailsListFragment extends BaseFragment {
    private static final String TAG = "MovDetailsListFragment";
    public static final String ARG_MOVIE_ID = "arg_movie_id";
    private MoviesDetailsListFragmentViewImpl mViewMvp;
    private int movieId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if ((args != null) && (args.containsKey(ARG_MOVIE_ID)))
            movieId = args.getInt(ARG_MOVIE_ID);
        else
            throw new IllegalStateException("MoviesDetailsListFragment must be started with movie ID argument");

        mViewMvp = new MoviesDetailsListFragmentViewImpl(inflater, container);
        return mViewMvp.getRootView();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewMvp.unbindButterKnife();
    }
}
