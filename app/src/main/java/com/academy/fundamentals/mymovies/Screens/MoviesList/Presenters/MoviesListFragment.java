package com.academy.fundamentals.mymovies.Screens.MoviesList.Presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.Screens.Common.Presenters.BaseFragment;
import com.academy.fundamentals.mymovies.Screens.MoviesList.MvpViews.MoviesListFragmentView;
import com.academy.fundamentals.mymovies.Screens.MoviesList.MvpViews.MoviesListFragmentViewImpl;


public class MoviesListFragment extends BaseFragment implements
        MoviesListFragmentView.MoviesListFragmentViewListener{
    private static final String TAG = "MoviesListFragment";
    private MoviesListFragmentViewImpl mViewMvp;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewMvp = new MoviesListFragmentViewImpl(inflater, container);
        mViewMvp.setListener(this);

        return mViewMvp.getRootView();
    }

    @Override
    public void onMovieClick(int movieIndex) {
        /*Bundle args = new Bundle(1);
        args.putInt(MovieDetailsFragment.ARG_SMS_MESSAGE_ID, movieIndex);

        replaceFragment(MovieDetailsFragment.class, true, args);*/
        Log.e(TAG, "myCheck onMovieClick " + movieIndex);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewMvp.unbindButterKnife();
    }
}
