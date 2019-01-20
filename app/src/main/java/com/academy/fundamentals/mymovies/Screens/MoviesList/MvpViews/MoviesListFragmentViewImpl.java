package com.academy.fundamentals.mymovies.Screens.MoviesList.MvpViews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
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

import com.academy.fundamentals.mymovies.Adapters.MoviesRecyclerAdapter;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MoviesListFragmentViewImpl implements MoviesListFragmentView,
        MoviesRecyclerAdapter.MoviesAdapterOnClickHandler {
    private static final String TAG = "MoviesListFtViewImpl";

    private View mRootView;
    private Unbinder unbinder;
    private MoviesListFragmentViewListener mListener;
    private MoviesRecyclerAdapter mMoviesRecyclerAdapter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.moviesRV) RecyclerView moviesRV;
    @BindView(R.id.loadingListPB) ProgressBar loadingPB;
    @BindView(R.id.emptyListCG) Group emptyListCG;
    @BindView(R.id.emptyListIV) ImageView emptyListIV;
    @BindView(R.id.emptyListTV) TextView emptyListTV;

    public MoviesListFragmentViewImpl(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_movies_list, container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        loadingPB.setVisibility(View.VISIBLE);

        confToolbar();
        confMoviesList();
    }

    @Override
    public void setToolbarTitle(String category) {
        toolbar.setTitle(category);
    }

    private void confToolbar() {
        toolbar.inflateMenu(R.menu.toolbar_menu_movies_list);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mListener != null) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.categoriesItem)
                        mListener.onCategoriesListClick();
                    else if (itemId == R.id.favoritesListItem)
                        mListener.onFavoritesListClick();
                }
                return true;
            }
        });
    }

    private void confMoviesList() {
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(mRootView.getContext(), LinearLayoutManager.VERTICAL, false);

        moviesRV.setLayoutManager(layoutManager);
        moviesRV.setHasFixedSize(true);   //Use this setting to improve performance if you know that changes in content do not change the child layout size

        moviesRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = layoutManager.getItemCount();
                int visibleItemCount = layoutManager.getChildCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if ((totalItemCount > visibleItemCount) &&
                        ((firstVisibleItem + visibleItemCount) >= (totalItemCount / 2))) {
                    if (mListener != null)
                        mListener.onListScroll();
                }
            }
        });
    }

    @Override
    public void onMovieClick(int selectedMoviePos) {
        if (mListener != null)
            mListener.onMovieClick(selectedMoviePos);
    }

    @Override
    public void setListener(MoviesListFragmentViewListener listener) {
        mListener = listener;
    }

    @Override
    public void displayMovies(List<Movie> movies) {
        loadingPB.setVisibility(View.GONE);

        if (mMoviesRecyclerAdapter == null) {
            moviesRV.scheduleLayoutAnimation();

            mMoviesRecyclerAdapter = new MoviesRecyclerAdapter(this, mRootView.getContext(), movies);
            moviesRV.setAdapter(mMoviesRecyclerAdapter);
        }
        else
            mMoviesRecyclerAdapter.appendMovies(movies);
    }

    @Override
    public void refreshList() {
        mMoviesRecyclerAdapter.notifyDataSetChanged();
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
            emptyListIV.setImageResource(R.drawable.ic_empty_list_no_results);
            emptyListTV.setText(R.string.text_view_empty_list_no_results);
        }
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