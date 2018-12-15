package com.academy.fundamentals.mymovies.Screens.MainMoviesList.Presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
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

import java.io.Serializable;
import java.util.List;


public class MainMoviesListFragment extends BaseFragment implements
        MainMoviesListFragmentView.MainMoviesListFragmentViewListener {
    private static final String TAG = "MainMoviesListFragment";

    private MainMoviesListFragmentViewImpl mViewMvp;
    private MoviesRepository moviesRepository;
    private List<Movie> currentMovies;
    private boolean isFetchingMovies;
    private int currentPage = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewMvp = new MainMoviesListFragmentViewImpl(inflater, container);
        mViewMvp.setListener(this);

        //Show the default fragment if the application is not restored
        if (savedInstanceState != null) {
            Log.e(TAG, "myCheck savedInstanceState != null");
        }
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

                currentMovies = movies;
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
    public void onMovieClick(int selectedMoviePos) {
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(MoviesDetailsListFragment.ARG_MOVIES, (Serializable) currentMovies);
        bundle.putInt(MoviesDetailsListFragment.ARG_SELECTED_MOVIE_POS, selectedMoviePos);

        replaceFragment(MoviesDetailsListFragment.class, true, bundle);
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
