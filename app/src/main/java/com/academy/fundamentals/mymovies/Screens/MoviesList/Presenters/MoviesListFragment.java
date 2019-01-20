package com.academy.fundamentals.mymovies.Screens.MoviesList.Presenters;

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
import com.academy.fundamentals.mymovies.R;
import com.academy.fundamentals.mymovies.Repositories.MoviesRepository;
import com.academy.fundamentals.mymovies.Screens.Common.Presenters.BaseFragment;
import com.academy.fundamentals.mymovies.Screens.FavoritesList.Presenters.FavoritesListFragment;
import com.academy.fundamentals.mymovies.Screens.MoviesList.MvpViews.MoviesListFragmentViewImpl;
import com.academy.fundamentals.mymovies.Screens.MoviesDetailsList.Presenters.MoviesDetailsListFragment;
import com.academy.fundamentals.mymovies.Screens.MoviesList.MvpViews.MoviesListFragmentView;

import java.io.Serializable;
import java.util.List;


public class MoviesListFragment extends BaseFragment implements
        MoviesListFragmentView.MoviesListFragmentViewListener {
    private static final String TAG = "MoviesListFragment";
    public static final String ARG_QUERY = "arg_query";
    public static final String ARG_CATEGORY = "arg_category";
    public static final String ARG_API_CATEGORY_NAME = "arg_api_category_name";
    public static final String ACTION_REFRESH_MOVIES_LIST = "action_refresh_movies_list";

    private MoviesListFragmentViewImpl mViewMvp;
    private String query;
    private String category;
    private String apiCategoryName;
    private MoviesRepository moviesRepository;
    private List<Movie> currentMovies;
    private boolean getMoviesByQuery, isFetchingMovies;
    private int currentPage = 1;
    private IntentFilter refreshListFilter;
    private boolean fragmentDestroyed;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if ((args != null) && (args.containsKey(ARG_QUERY))) {
            query = args.getString(ARG_QUERY);
            getMoviesByQuery = true;
        }
        else if ((args != null) && (args.containsKey(ARG_CATEGORY)) &&
                (args.containsKey(ARG_API_CATEGORY_NAME))) {
            category = args.getString(ARG_CATEGORY);
            apiCategoryName = args.getString(ARG_API_CATEGORY_NAME);
        }
        else
            throw new IllegalStateException("MoviesListFragment must be started " +
                    "with either search query or category and apiCategoryName strings");

        mViewMvp = new MoviesListFragmentViewImpl(inflater, container);

        init(inflater.getContext());

        return mViewMvp.getRootView();
    }

    private void init(Context context) {
        mViewMvp.setListener(this);

        if (getMoviesByQuery) {
            String toolbarTitle = query.replace("", " ").trim();
            mViewMvp.setToolbarTitle(toolbarTitle);
        }
        else
            mViewMvp.setToolbarTitle(category);

        refreshListFilter = new IntentFilter(ACTION_REFRESH_MOVIES_LIST);

        moviesRepository = MoviesRepository.getInstance();

        /* Start the "get's" operations only after the fragment switch animation has finished
           in order to avoid stutters in the UI */
        mViewMvp.getRootView().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (moviesRepository.getGenres() == null)
                    getGenres();
                else
                    getMovies(currentPage);
            }
        }, context.getResources().
                getInteger(R.integer.animation_duration_switching_fragment));
    }

    private void getGenres() {
        moviesRepository.getGenres(new OnGetGenresCallback() {
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

        if (getMoviesByQuery) {
            moviesRepository.getMoviesByQuery(query, page, new OnGetMoviesCallback() {
                @Override
                public void onSuccess(List<Movie> movies, int page) {
                    if (!fragmentDestroyed) {
                        if (movies.size() > 0)
                            getMoviesSucceeded(movies, page);
                        else
                            mViewMvp.displayEmptyList(false);
                    }
                }

                @Override
                public void onError() {
                    if (!fragmentDestroyed)
                        mViewMvp.displayEmptyList(true);
                }
            });
        }
        else {
            moviesRepository.getMoviesByCategory(apiCategoryName, page, new OnGetMoviesCallback() {
                @Override
                public void onSuccess(List<Movie> movies, int page) {
                    if (!fragmentDestroyed)
                        getMoviesSucceeded(movies, page);
                }

                @Override
                public void onError() {
                    if (!fragmentDestroyed)
                        mViewMvp.displayEmptyList(true);
                }
            });
        }
    }

    private void getMoviesSucceeded(List<Movie> movies, int page) {
        mViewMvp.displayMovies(movies);

        if (currentMovies == null)
            currentMovies = movies;
        else
            currentMovies.addAll(movies);

        currentPage = page;
        isFetchingMovies = false;
    }

    @Override
    public void onCategoriesListClick() {
        getActivity().getSupportFragmentManager().popBackStack();
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
        bundle.putBoolean(MoviesDetailsListFragment.ARG_IS_FAVORITES_ADAPTER, false);

        replaceFragment(MoviesDetailsListFragment.class, true, bundle);
    }

    @Override
    public void onListScroll() {
        if (!isFetchingMovies)
            getMovies(currentPage + 1);
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

        fragmentDestroyed = true;
    }
}