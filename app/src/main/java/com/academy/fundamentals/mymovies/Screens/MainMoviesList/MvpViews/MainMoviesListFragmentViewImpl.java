package com.academy.fundamentals.mymovies.Screens.MainMoviesList.MvpViews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.Adapters.MoviesRecyclerAdapter;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MainMoviesListFragmentViewImpl implements MainMoviesListFragmentView,
        MoviesRecyclerAdapter.MoviesAdapterOnClickHandler {
    private static final String TAG = "MainMovListFtViewImpl";

    private View mRootView;
    private Unbinder unbinder;
    private MainMoviesListFragmentViewListener mListener;
    private MoviesRecyclerAdapter mMoviesRecyclerAdapter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.moviesRV) RecyclerView moviesRV;

    public MainMoviesListFragmentViewImpl(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_main_movies_list, container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        confToolbar();
        confMoviesList();
    }

    private void confToolbar() {
        toolbar.inflateMenu(R.menu.toolbar_menu_main_list);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mListener != null)
                    mListener.onFavoritesListClick();

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

                if ((firstVisibleItem + visibleItemCount) >= (totalItemCount / 2)) {
                    if (mListener != null)
                        mListener.onListScroll();
                }
            }
        });
    }

    @Override
    public void onClick(int selectedMoviePos) {
        if (mListener != null) {
            mListener.onMovieClick(selectedMoviePos);
        }
    }

    @Override
    public void setListener(MainMoviesListFragmentViewListener listener) {
        mListener = listener;
    }

    @Override
    public void displayMovies(List<Movie> movies) {
        if (mMoviesRecyclerAdapter == null) {
            mMoviesRecyclerAdapter = new MoviesRecyclerAdapter(mRootView.getContext(), this, movies);
            moviesRV.setAdapter(mMoviesRecyclerAdapter);
        }
        else
            mMoviesRecyclerAdapter.appendMovies(movies);
    }

    @Override
    public void getMoviesFailed() {
        Log.e(TAG, "Error, couldn't get movies");
    }

    @Override
    public void refreshList() {
        mMoviesRecyclerAdapter.notifyDataSetChanged();
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
