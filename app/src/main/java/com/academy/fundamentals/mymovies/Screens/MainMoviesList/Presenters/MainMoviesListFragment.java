package com.academy.fundamentals.mymovies.Screens.MainMoviesList.Presenters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.Callbacks.OnGetGenresCallback;
import com.academy.fundamentals.mymovies.Models.Genre;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Callbacks.OnGetMoviesCallback;
import com.academy.fundamentals.mymovies.Repositories.MoviesRepository;
import com.academy.fundamentals.mymovies.Screens.Common.Presenters.BaseFragment;
import com.academy.fundamentals.mymovies.Screens.FavoritesList.Presenters.FavoritesListFragment;
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

        moviesRepository = MoviesRepository.getInstance();

        if (moviesRepository.getGenres() == null)
            getGenres();
        else
            getMovies(currentPage);

        return mViewMvp.getRootView();
    }

    private void getGenres() {
        moviesRepository.getGenres(getContext(), new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genresList) {
                moviesRepository.setGenres(genresList);
                getMovies(currentPage);
            }

            @Override
            public void onError() {
            }
        });
    }

    private void getMovies(int page) {
        isFetchingMovies = true;

        moviesRepository.getPopularMovies(page, getContext(), new OnGetMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies, int page) {
                mViewMvp.displayMovies(movies);

                if (currentMovies == null)
                    currentMovies = movies;
                else
                    currentMovies.addAll(movies);

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
    public void onFavoritesListClick() {
        replaceFragment(FavoritesListFragment.class, true, null);
    }

    @Override
    public void onMovieClick(int selectedMoviePos) {
        Bundle bundle = new Bundle(3);
        bundle.putSerializable(MoviesDetailsListFragment.ARG_MOVIES, (Serializable) currentMovies);
        bundle.putSerializable(MoviesDetailsListFragment.ARG_GENRES, (Serializable) moviesRepository.getGenres());
        bundle.putInt(MoviesDetailsListFragment.ARG_SELECTED_MOVIE_POS, selectedMoviePos);

        replaceFragment(MoviesDetailsListFragment.class, true, bundle);
    }

    @Override
    public void onListScroll() {
        if (!isFetchingMovies) {
            getMovies(currentPage + 1);
        }
    }

    private BroadcastReceiver refreshListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent ) {
            mViewMvp.refreshList();
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter refreshListFilter = new IntentFilter(MoviesDetailsListFragment.ACTION_REFRESH_LIST);

        LocalBroadcastManager.getInstance(mViewMvp.getRootView().getContext()).
                registerReceiver(refreshListReceiver, refreshListFilter);
    }

    @Override
    public void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(mViewMvp.getRootView().getContext())
                .unregisterReceiver(refreshListReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewMvp.unbindButterKnife();
    }
}