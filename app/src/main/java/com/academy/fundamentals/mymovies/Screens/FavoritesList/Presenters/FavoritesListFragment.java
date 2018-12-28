package com.academy.fundamentals.mymovies.Screens.FavoritesList.Presenters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.Callbacks.OnGetGenresCallback;
import com.academy.fundamentals.mymovies.Callbacks.OnGetMovieCallback;
import com.academy.fundamentals.mymovies.Models.Genre;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Repositories.FavoritesRepository;
import com.academy.fundamentals.mymovies.Repositories.MoviesRepository;
import com.academy.fundamentals.mymovies.Screens.Common.Presenters.BaseFragment;
import com.academy.fundamentals.mymovies.Screens.FavoritesList.MvpViews.FavoritesListFragmentView;
import com.academy.fundamentals.mymovies.Screens.FavoritesList.MvpViews.FavoritesListFragmentViewImpl;
import com.academy.fundamentals.mymovies.Screens.MoviesDetailsList.Presenters.MoviesDetailsListFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class FavoritesListFragment extends BaseFragment implements
        FavoritesListFragmentView.FavoritesListFragmentViewListener {
    private static final String TAG = "FavoritesListFragment";

    private FavoritesListFragmentViewImpl mViewMvp;
    private FavoritesRepository favoritesRepository;
    private MoviesRepository moviesRepository;
    private List<Movie> movies = new ArrayList<>();
    private List<Genre> genres;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewMvp = new FavoritesListFragmentViewImpl(inflater, container);
        mViewMvp.setListener(this);

        favoritesRepository = FavoritesRepository.getInstance(inflater.getContext());
        moviesRepository = MoviesRepository.getInstance();
        getGenres();

        return mViewMvp.getRootView();
    }

    private void getGenres() {
        moviesRepository.getGenres(getContext(), new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genresList) {
                genres = genresList;
                getMovieIds();
            }

            @Override
            public void onError() {
            }
        });
    }

    private void getMovieIds() {
        Set<String> movieIds = favoritesRepository.getItems();

        if (movieIds.isEmpty()) {
            mViewMvp.displayEmptyList();
            return;
        }

        for(String movieId : movieIds)
            getMovie(Integer.parseInt(movieId));
    }

    private void getMovie(final int movieId) {
        moviesRepository.getMovie(movieId, getContext(), new OnGetMovieCallback() {
            @Override
            public void onSuccess(Movie movie) {
                movies.add(movie);
                mViewMvp.displayMovie(movie);
            }

            @Override
            public void onError() {
                mViewMvp.getMovieFailed(movieId);
            }
        });
    }

    @Override
    public void onMovieClick(int selectedMoviePos) {
        Bundle bundle = new Bundle(3);
        bundle.putSerializable(MoviesDetailsListFragment.ARG_MOVIES, (Serializable) movies);
        bundle.putSerializable(MoviesDetailsListFragment.ARG_GENRES, (Serializable) genres);
        bundle.putInt(MoviesDetailsListFragment.ARG_SELECTED_MOVIE_POS, selectedMoviePos);

        replaceFragment(MoviesDetailsListFragment.class, true, bundle);
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
