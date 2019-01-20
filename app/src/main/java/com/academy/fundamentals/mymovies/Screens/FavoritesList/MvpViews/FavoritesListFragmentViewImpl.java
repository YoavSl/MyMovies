package com.academy.fundamentals.mymovies.Screens.FavoritesList.MvpViews;

import android.os.Bundle;
import android.support.constraint.Group;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.academy.fundamentals.mymovies.Adapters.FavoriteMoviesRecyclerAdapter;
import com.academy.fundamentals.mymovies.Adapters.SortModesRecyclerAdapter;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Models.SortType;
import com.academy.fundamentals.mymovies.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FavoritesListFragmentViewImpl implements FavoritesListFragmentView,
        SortModesRecyclerAdapter.SortModesAdapterOnClickHandler,
        FavoriteMoviesRecyclerAdapter.FavoriteMoviesAdapterOnClickHandler {
    private static final String TAG = "FavoritesListFtViewImpl";

    private View mRootView;
    private Unbinder unbinder;
    private FavoritesListFragmentViewListener mListener;
    private FavoriteMoviesRecyclerAdapter mFavoriteMoviesRecyclerAdapter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.sortMenuEL) ExpandableLayout sortMenuEL;
    @BindView(R.id.sortModesRV) RecyclerView sortModesRV;
    @BindView(R.id.moviesRV) RecyclerView moviesRV;
    @BindView(R.id.loadingListPB) ProgressBar loadingPB;
    @BindView(R.id.emptyListCG) Group emptyListCG;
    @BindView(R.id.emptyListIV) ImageView emptyListIV;
    @BindView(R.id.emptyListTV) TextView emptyListTV;

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
    public void onMovieLongClick(int selectedMoviePos) {
        if (mListener != null)
            mListener.onMovieLongClick(selectedMoviePos);
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

        mFavoriteMoviesRecyclerAdapter = new FavoriteMoviesRecyclerAdapter( this, mRootView.getContext(), movies);
        moviesRV.setAdapter(mFavoriteMoviesRecyclerAdapter);
    }

    @Override
    public void updateMoviesOrder(List<Movie> movies) {
        mFavoriteMoviesRecyclerAdapter.updateMoviesOrder(movies);
    }

    @Override
    public void removeMovie(int pos) {
        mFavoriteMoviesRecyclerAdapter.removeMovie(pos);
    }

    @Override
    public void displayMoviesCount(int moviesCount) {
        toolbar.setTitle(mRootView.getContext().getString(
                    R.string.toolbar_title_favorites_not_empty, moviesCount));
    }

    @Override
    public void displayEmptyList(boolean error) {
        loadingPB.setVisibility(View.GONE);
        emptyListCG.setVisibility(View.VISIBLE);

        if (error) {
            emptyListIV.setImageResource(R.drawable.ic_empty_list_error);
            emptyListTV.setText(R.string.text_view_empty_list_error);
        }
        else {
            emptyListIV.setImageResource(R.drawable.ic_empty_list_no_favorites);
            emptyListTV.setText(R.string.text_view_empty_list_no_favorites);
        }
    }

    @Override
    public void getMoviesFailed(boolean failedEntirely) {
        if (failedEntirely)
            displayEmptyList(true);
        else
            Snackbar.make(mRootView, R.string.snackbar_couldnt_retrieve_all_favorites,
                    Snackbar.LENGTH_LONG).show();
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