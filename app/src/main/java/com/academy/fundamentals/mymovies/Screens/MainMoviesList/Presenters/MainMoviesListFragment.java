package com.academy.fundamentals.mymovies.Screens.MainMoviesList.Presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.OnGetMoviesCallback;
import com.academy.fundamentals.mymovies.Repositories.MoviesRepository;
import com.academy.fundamentals.mymovies.Screens.Common.Presenters.BaseFragment;
import com.academy.fundamentals.mymovies.Screens.MainMoviesList.MvpViews.MainMoviesListFragmentViewImpl;
import com.academy.fundamentals.mymovies.Screens.MoviesDetailsList.Presenters.MoviesDetailsListFragment;
import com.academy.fundamentals.mymovies.Screens.MainMoviesList.MvpViews.MainMoviesListFragmentView;

import java.util.List;


public class MainMoviesListFragment extends BaseFragment implements
        MainMoviesListFragmentView.MainMoviesListFragmentViewListener {
    private static final String TAG = "MainMoviesListFragment";
    private MainMoviesListFragmentViewImpl mViewMvp;
    private MoviesRepository moviesRepository;
    private boolean isFetchingMovies;
    private int currentPage = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewMvp = new MainMoviesListFragmentViewImpl(inflater, container);
        mViewMvp.setListener(this);

        moviesRepository = MoviesRepository.getInstance();
        getMovies(currentPage);

        return mViewMvp.getRootView();
    }

    private void getMovies(int page) {
        isFetchingMovies = true;

        moviesRepository.getPopularMovies(getContext(), page, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies, int page) {
                mViewMvp.displayMovies(movies);

                currentPage = page;
                isFetchingMovies = false;
            }

            @Override
            public void onError() {
                mViewMvp.getMoviesFailed();
            }
        });
    }

    @Override
    public void onMovieClick(int movieId) {
        Bundle args = new Bundle(1);
        args.putInt(MoviesDetailsListFragment.ARG_MOVIE_ID, movieId);

        replaceFragment(MoviesDetailsListFragment.class, true, args);
    }

    @Override
    public void onListScroll() {
        if (!isFetchingMovies) {
            getMovies(currentPage + 1);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewMvp.unbindButterKnife();
    }
}
