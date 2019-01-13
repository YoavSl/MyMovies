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
    public static final String ARG_CATEGORY = "arg_category";
    public static final String ARG_API_CATEGORY_NAME = "arg_api_category_name";

    private MoviesListFragmentViewImpl mViewMvp;
    private String category;
    private String apiCategoryName;
    private MoviesRepository moviesRepository;
    private List<Movie> currentMovies;
    private boolean isFetchingMovies;
    private int currentPage = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if ((args != null) && (args.containsKey(ARG_CATEGORY)) &&
                (args.containsKey(ARG_API_CATEGORY_NAME))) {
            category = args.getString(ARG_CATEGORY);
            apiCategoryName = args.getString(ARG_API_CATEGORY_NAME);
        }
        else
            throw new IllegalStateException("MoviesListFragment must be started " +
                    "with category and apiCategoryName strings");

        mViewMvp = new MoviesListFragmentViewImpl(inflater, container);
        mViewMvp.setListener(this);
        mViewMvp.setToolbarTitle(category);

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
        }, inflater.getContext().getResources().getInteger(R.integer.animation_duration_switching_fragment));

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

        moviesRepository.getMoviesByCategory(apiCategoryName, page, getContext(), new OnGetMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies, int page) {
                getMoviesSucceeded(movies, page);
            }

            @Override
            public void onError() {
                mViewMvp.getMoviesFailed();
            }
        });
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