package com.academy.fundamentals.mymovies.Screens.HomeScreen.Presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.Screens.HomeScreen.MvpViews.HomeScreenFragmentView;
import com.academy.fundamentals.mymovies.Screens.HomeScreen.MvpViews.HomeScreenFragmentViewImpl;
import com.academy.fundamentals.mymovies.Screens.Common.Presenters.BaseFragment;
import com.academy.fundamentals.mymovies.Screens.FavoritesList.Presenters.FavoritesListFragment;
import com.academy.fundamentals.mymovies.Screens.MoviesList.Presenters.MoviesListFragment;


public class HomeScreenFragment extends BaseFragment implements
        HomeScreenFragmentView.HomeScreenFragmentViewListener {
    private static final String TAG = "CategoriesFragment";

    private HomeScreenFragmentViewImpl mViewMvp;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewMvp = new HomeScreenFragmentViewImpl(inflater, container);
        mViewMvp.setListener(this);

        return mViewMvp.getRootView();
    }

    @Override
    public void onFavoritesListClick() {
        replaceFragment(FavoritesListFragment.class, true, null);
    }

    @Override
    public void onSearchExecution(String query) {
        Bundle bundle = new Bundle(1);
        bundle.putString(MoviesListFragment.ARG_QUERY, query);

        replaceFragment(MoviesListFragment.class, true, bundle);
    }

    @Override
    public void onCategoryClick(String category, String apiCategoryName) {
        Bundle bundle = new Bundle(2);
        bundle.putString(MoviesListFragment.ARG_CATEGORY, category);
        bundle.putString(MoviesListFragment.ARG_API_CATEGORY_NAME, apiCategoryName);

        replaceFragment(MoviesListFragment.class, true, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewMvp.unbindButterKnife();
    }
}