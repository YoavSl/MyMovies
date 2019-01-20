package com.academy.fundamentals.mymovies.Screens.FavoritesList.Presenters;

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
import com.academy.fundamentals.mymovies.Callbacks.OnGetMovieCallback;
import com.academy.fundamentals.mymovies.HelperClasses.SortMovies;
import com.academy.fundamentals.mymovies.Models.Genre;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Models.SortType;
import com.academy.fundamentals.mymovies.R;
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

import static com.academy.fundamentals.mymovies.Screens.MovieDetails.Presenters.MovieDetailsFragment.ARG_MOVIE_ID;
import static com.academy.fundamentals.mymovies.Screens.MoviesList.Presenters.MoviesListFragment.ACTION_REFRESH_MOVIES_LIST;


public class FavoritesListFragment extends BaseFragment implements
        FavoritesListFragmentView.FavoritesListFragmentViewListener {
    private static final String TAG = "FavoritesListFragment";
    public static final String ACTION_REMOVE_FAVORITE_MOVIE = "action_remove_favorite_movie";

    public static SortType DEFAULT_SORT_TYPE = SortType.NAME;
    public static boolean DEFAULT_ASCENDING_SORT = true;

    private FavoritesListFragmentViewImpl mViewMvp;
    private MoviesRepository moviesRepository;
    private FavoritesRepository favoritesRepository;
    private List<Movie> movies = new ArrayList<>();
    private Set<String> movieIds;
    private int totalMoviesCounter, moviesLoadedCounter = 0, moviesNotLoadedCounter = 0;
    private IntentFilter removeMovieFilter;
    private boolean fragmentDestroyed;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewMvp = new FavoritesListFragmentViewImpl(inflater, container);
        init(inflater.getContext());

        return mViewMvp.getRootView();
    }

    private void init(Context context) {
        mViewMvp.setListener(this);

        removeMovieFilter = new IntentFilter(ACTION_REMOVE_FAVORITE_MOVIE);

        moviesRepository = MoviesRepository.getInstance();
        favoritesRepository = FavoritesRepository.getInstance(context);

        getMovieIds();
    }

    private void getGenres() {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genresList) {
                moviesRepository.setGenres(genresList);
                getMovies();
            }

            @Override
            public void onError() {
            }
        });
    }

    private void getMovieIds() {
        movieIds = favoritesRepository.getItems();
        totalMoviesCounter = movieIds.size();

        if (totalMoviesCounter > 0) {
            mViewMvp.displayMoviesCount(totalMoviesCounter);
            mViewMvp.displayLoadingAnimation();

            mViewMvp.setSortMenu();

            mViewMvp.getRootView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (moviesRepository.getGenres() == null)
                        getGenres();
                    else
                        getMovies();
                }
            }, mViewMvp.getRootView().getContext().
                    getResources().getInteger(R.integer.animation_duration_switching_fragment));
        }
        else
            mViewMvp.displayEmptyList(false);
    }

    private void getMovies() {
        for(String movieId : movieIds)
            getMovie(Integer.parseInt(movieId));
    }

    private void getMovie(final int movieId) {
        moviesRepository.getMovieById(movieId, new OnGetMovieCallback() {
            @Override
            public void onSuccess(Movie movie) {
                if (!fragmentDestroyed) {
                    movies.add(movie);
                    moviesLoadedCounter++;

                    if (totalMoviesCounter == (moviesLoadedCounter + moviesNotLoadedCounter)) {
                        if (totalMoviesCounter != moviesLoadedCounter)
                            mViewMvp.getMoviesFailed(false);

                        getMoviesFinished();
                    }
                }
            }

            @Override
            public void onError() {
                if (!fragmentDestroyed) {
                    moviesNotLoadedCounter++;

                    if (totalMoviesCounter == (moviesLoadedCounter + moviesNotLoadedCounter)) {
                        if (moviesLoadedCounter == 0)
                            mViewMvp.getMoviesFailed(true);
                        else {
                            mViewMvp.getMoviesFailed(false);
                            getMoviesFinished();
                        }
                    }
                }
            }
        });
    }

    private void getMoviesFinished() {
        sortMovies(FavoritesListFragment.DEFAULT_SORT_TYPE, FavoritesListFragment.DEFAULT_ASCENDING_SORT);
        mViewMvp.displayMovies(movies);
    }

    @Override
    public void onMovieClick(int selectedMoviePos) {
        Bundle bundle = new Bundle(3);
        bundle.putSerializable(MoviesDetailsListFragment.ARG_MOVIES, (Serializable) movies);
        bundle.putSerializable(MoviesDetailsListFragment.ARG_GENRES, (Serializable) moviesRepository.getGenres());
        bundle.putInt(MoviesDetailsListFragment.ARG_SELECTED_MOVIE_POS, selectedMoviePos);
        bundle.putInt(MoviesDetailsListFragment.ARG_SELECTED_MOVIE_POS, selectedMoviePos);
        bundle.putBoolean(MoviesDetailsListFragment.ARG_IS_FAVORITES_ADAPTER, true);

        replaceFragment(MoviesDetailsListFragment.class, true, bundle);
    }

    @Override
    public void onMovieLongClick(int selectedMoviePos) {
        reduceMoviesCount();
        movies.remove(selectedMoviePos);
    }

    private void reduceMoviesCount() {
        totalMoviesCounter--;
        mViewMvp.displayMoviesCount(totalMoviesCounter);
    }

    @Override
    public void onSortModeClick(SortType sortType, boolean ascendingSort) {
        sortMovies(sortType, ascendingSort);
        mViewMvp.updateMoviesOrder(movies);
    }

    public void sortMovies(SortType sortType, boolean ascendingSort) {
        movies = SortMovies.sortByType(movies, sortType, ascendingSort);
    }

    private BroadcastReceiver removeMovieReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent ) {
            int movieId = intent.getIntExtra(ARG_MOVIE_ID, 0);

            if (movieId != 0) {
                for (int i = 0; i < movies.size(); i++)
                    if (movies.get(i).getId() == movieId) {
                        mViewMvp.removeMovie(i);
                        reduceMoviesCount();

                        return;
                    }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();

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

        fragmentDestroyed = true;

        LocalBroadcastManager.getInstance(mViewMvp.getRootView().getContext()).
                sendBroadcast(new Intent(ACTION_REFRESH_MOVIES_LIST));
    }
}