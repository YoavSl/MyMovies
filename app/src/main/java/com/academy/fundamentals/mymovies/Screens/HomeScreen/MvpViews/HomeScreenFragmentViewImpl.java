package com.academy.fundamentals.mymovies.Screens.HomeScreen.MvpViews;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class HomeScreenFragmentViewImpl implements HomeScreenFragmentView {
    private static final String TAG = "HomeScreenFtViewImpl";

    private View mRootView;
    private Unbinder unbinder;
    private HomeScreenFragmentViewListener mListener;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.searchSV) SearchView searchSV;

    public HomeScreenFragmentViewImpl(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        confToolbar();
        confSearch();
    }

    private void confToolbar() {
        toolbar.inflateMenu(R.menu.toolbar_menu_categories);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mListener != null) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.favoritesListItem)
                        mListener.onFavoritesListClick();
                }
                return true;
            }
        });
    }

    private void confSearch() {
        searchSV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onSearchExecution(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void onSearchExecution(String query) {
        if (mListener != null)
            mListener.onSearchExecution(query);
    }

    @OnClick({R.id.mostPopularCategoryCV, R.id.nowPlayingCategoryCV,
            R.id.upcomingCategoryCV, R.id.topRatedCategoryCV})
    void OnClick(View view) {
        if (mListener != null) {
            Context context = getRootView().getContext();

            switch (view.getId()) {
                case R.id.mostPopularCategoryCV:
                    onCategoryClick(context.getString(R.string.category_popular),
                            context.getString(R.string.api_category_popular));
                    break;
                case R.id.nowPlayingCategoryCV:
                    onCategoryClick(context.getString(R.string.category_now_playing),
                            context.getString(R.string.api_category_now_playing));
                    break;
                case R.id.upcomingCategoryCV:
                    onCategoryClick(context.getString(R.string.category_upcoming),
                            context.getString(R.string.api_category_upcoming));
                    break;
                case R.id.topRatedCategoryCV:
                    onCategoryClick(context.getString(R.string.category_top_rated),
                            context.getString(R.string.api_category_top_rated));
                    break;
            }
        }
    }

    private void onCategoryClick(String category, String apiCategoryName) {
        if (mListener != null)
            mListener.onCategoryClick(category, apiCategoryName);
    }

    @Override
    public void setListener(HomeScreenFragmentViewListener listener) {
        mListener = listener;
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