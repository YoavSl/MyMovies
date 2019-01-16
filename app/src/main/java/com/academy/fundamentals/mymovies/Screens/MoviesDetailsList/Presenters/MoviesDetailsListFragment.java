package com.academy.fundamentals.mymovies.Screens.MoviesDetailsList.Presenters;

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

import com.academy.fundamentals.mymovies.Models.Genre;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Screens.Common.Presenters.BaseFragment;
import com.academy.fundamentals.mymovies.Screens.MoviesDetailsList.MvpViews.MoviesDetailsListFragmentViewImpl;

import java.util.List;

import static com.academy.fundamentals.mymovies.Screens.FavoritesList.Presenters.FavoritesListFragment.ACTION_REMOVE_FAVORITE_MOVIE;
import static com.academy.fundamentals.mymovies.Screens.MovieDetails.Presenters.MovieDetailsFragment.ARG_MOVIE_ID;
import static com.academy.fundamentals.mymovies.Screens.MoviesList.Presenters.MoviesListFragment.ACTION_REFRESH_MOVIES_LIST;


public class MoviesDetailsListFragment extends BaseFragment {
    private static final String TAG = "MovDetailsListFragment";
    public static final String ARG_MOVIES = "arg_movies";
    public static final String ARG_GENRES = "arg_genres";
    public static final String ARG_SELECTED_MOVIE_POS = "arg_selected_movie_pos";
    public static final String ARG_IS_FAVORITES_ADAPTER = "arg_is_favorites_adapter";

    private MoviesDetailsListFragmentViewImpl mViewMvp;
    private List<Movie> movies;
    private List<Genre> genres;
    private int selectedMoviePos;
    private boolean isFavoritesAdapter;
    private IntentFilter removeMovieFilter;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if ((args != null) && (args.containsKey(ARG_MOVIES)) && (args.containsKey(ARG_GENRES))
                && (args.containsKey(ARG_SELECTED_MOVIE_POS))) {
            movies = (List<Movie>) args.getSerializable(ARG_MOVIES);
            genres = (List<Genre>) args.getSerializable(ARG_GENRES);
            selectedMoviePos = args.getInt(ARG_SELECTED_MOVIE_POS);
            isFavoritesAdapter = args.getBoolean(ARG_IS_FAVORITES_ADAPTER);
        }
        else
            throw new IllegalStateException(
                    "MoviesDetailsListFragment must be started with a Movies and Genres arrays, " +
                            "selected movie pos argument and a boolean value specifying if" +
                            "it's a regular/favorites movies adapter");

        mViewMvp = new MoviesDetailsListFragmentViewImpl(inflater, container);
        init();

        return mViewMvp.getRootView();
    }

    private void init() {
        mViewMvp.confMoviesList(movies, genres, selectedMoviePos);

        if (isFavoritesAdapter)
            removeMovieFilter = new IntentFilter(ACTION_REMOVE_FAVORITE_MOVIE);
    }

    private BroadcastReceiver removeMovieReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent ) {
            if (movies.size() == 1)
                getActivity().getSupportFragmentManager().popBackStack();
            else {
                int movieId = intent.getIntExtra(ARG_MOVIE_ID, 0);

                if (movieId != 0) {
                    for (int i = 0; i < movies.size(); i++)
                        if (movies.get(i).getId() == movieId) {
                            mViewMvp.removeMovie(i);

                            return;
                        }
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        if (isFavoritesAdapter)
            LocalBroadcastManager.getInstance(mViewMvp.getRootView().getContext()).
                    registerReceiver(removeMovieReceiver, removeMovieFilter);
    }

    @Override
    public void onStop() {
        super.onStop();

            LocalBroadcastManager.getInstance(mViewMvp.getRootView().getContext())
                    .unregisterReceiver(removeMovieReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mViewMvp.unbindButterKnife();
        LocalBroadcastManager.getInstance(mViewMvp.getRootView().getContext()).
                sendBroadcast(new Intent(ACTION_REFRESH_MOVIES_LIST));
    }
}