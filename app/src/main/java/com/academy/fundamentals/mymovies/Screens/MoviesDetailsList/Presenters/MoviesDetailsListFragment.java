package com.academy.fundamentals.mymovies.Screens.MoviesDetailsList.Presenters;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Screens.Common.Presenters.BaseFragment;
import com.academy.fundamentals.mymovies.Screens.MoviesDetailsList.MvpViews.MoviesDetailsListFragmentView;
import com.academy.fundamentals.mymovies.Screens.MoviesDetailsList.MvpViews.MoviesDetailsListFragmentViewImpl;

import java.io.UncheckedIOException;
import java.util.List;


public class MoviesDetailsListFragment extends BaseFragment {
    private static final String TAG = "MovDetailsListFragment";
    public static final String ARG_MOVIES = "arg_movies";
    public static final String ARG_SELECTED_MOVIE_POS = "arg_selected_movie_pos";

    private MoviesDetailsListFragmentViewImpl mViewMvp;
    private List<Movie> movies;
    private int selectedMoviePos;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewMvp = new MoviesDetailsListFragmentViewImpl(inflater, container);

        Bundle args = getArguments();
        if ((args != null) && (args.containsKey(ARG_MOVIES) && (args.containsKey(ARG_SELECTED_MOVIE_POS)))) {
            movies = (List<Movie>) args.getSerializable(ARG_MOVIES);
            selectedMoviePos = args.getInt(ARG_SELECTED_MOVIE_POS);
        }
        else
            throw new IllegalStateException(
                    "MoviesDetailsListFragment must be started with a Movies array and selected movie pos argument");

        mViewMvp.confMoviesList(movies, selectedMoviePos);

        return mViewMvp.getRootView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewMvp.unbindButterKnife();
    }
}
