package com.academy.fundamentals.mymovies.Screens.FavoritesList.MvpViews;

import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.academy.fundamentals.mymovies.Adapters.MoviesRecyclerAdapter;
import com.academy.fundamentals.mymovies.Adapters.SortModesRecyclerAdapter;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Models.SortType;
import com.academy.fundamentals.mymovies.R;
import com.academy.fundamentals.mymovies.Screens.FavoritesList.Presenters.FavoritesListFragment;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FavoritesListFragmentViewImpl implements FavoritesListFragmentView,
        SortModesRecyclerAdapter.SortModesAdapterOnClickHandler,
        MoviesRecyclerAdapter.MoviesAdapterOnClickHandler {
    private static final String TAG = "FavoritesListFtViewImpl";

    private View mRootView;
    private Unbinder unbinder;
    private FavoritesListFragmentViewListener mListener;
    private MoviesRecyclerAdapter mMoviesRecyclerAdapter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.sortMenuEL) ExpandableLayout sortMenuEL;
    @BindView(R.id.sortModesRV) RecyclerView sortModesRV;
    @BindView(R.id.moviesRV) RecyclerView moviesRV;
    @BindView(R.id.noFavoritesCG) Group noFavoritesCG;
    @BindView(R.id.loadingListPB) ProgressBar loadingPB;

    public FavoritesListFragmentViewImpl(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_favorites_list, container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        confMoviesList();
    }

    @Override
    public void setSortMenu() {
        confToolbar();
        confSortMenu();
    }

    private void confToolbar() {
        toolbar.inflateMenu(R.menu.toolbar_menu_favorites_list);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.sortItem)
                    sortMenuEL.toggle();
                return true;
            }
        });
    }

    private void confSortMenu() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mRootView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        sortModesRV.setLayoutManager(layoutManager);

        SortModesRecyclerAdapter mSortModesRecyclerAdapter = new SortModesRecyclerAdapter(this);
        sortModesRV.setAdapter(mSortModesRecyclerAdapter);
    }

    private void confMoviesList() {
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(mRootView.getContext(), LinearLayoutManager.VERTICAL, false);

        moviesRV.setLayoutManager(layoutManager);
        moviesRV.setHasFixedSize(true);
    }

    @Override
    public void onSortModeClick(SortType sortType, boolean ascendingSort) {
        if (mListener != null)
            mListener.onSortModeClick(sortType, ascendingSort);
    }

    @Override
    public void onMovieClick(int selectedMoviePos) {
        if (mListener != null) {
            mListener.onMovieClick(selectedMoviePos);
        }
    }

    @Override
    public void setListener(FavoritesListFragmentViewListener listener) {
        mListener = listener;
    }

    @Override
    public void displayLoadingAnimation() {
        loadingPB.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayMovies(List<Movie> movies) {
        loadingPB.setVisibility(View.GONE);
        moviesRV.scheduleLayoutAnimation();

        mMoviesRecyclerAdapter = new MoviesRecyclerAdapter(this, mRootView.getContext(), movies);
        moviesRV.setAdapter(mMoviesRecyclerAdapter);

        onSortModeClick(FavoritesListFragment.DEFAULT_SORT_TYPE, FavoritesListFragment.DEFAULT_ASCENDING_SORT);
    }

    @Override
    public void displayEmptyList() {
        noFavoritesCG.setVisibility(View.VISIBLE);
    }

    @Override
    public void getMovieFailed(int movieId) {
        Log.e(TAG, "Error, couldn't get movie (id: " + movieId + ")");
    }

    @Override
    public void updateMoviesOrder(List<Movie> movies) {
        mMoviesRecyclerAdapter.updateMoviesOrder(movies);
    }

    @Override
    public void refreshList() {
        mMoviesRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayMoviesCount(int moviesCount) {
        if (moviesCount > 0)
            toolbar.setTitle(mRootView.getContext().getString(
                    R.string.toolbar_title_favorites_not_empty, moviesCount));
        else
            toolbar.setTitle(mRootView.getContext().getString(
                    R.string.toolbar_title_favorites_empty));
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    @Override
    public void unbindButterKnife() {
        unbinder.unbind();
    }
}