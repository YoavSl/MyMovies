package com.academy.fundamentals.mymovies.Screens.CategoriesList.Presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.Screens.CategoriesList.MvpViews.CategoriesFragmentView;
import com.academy.fundamentals.mymovies.Screens.CategoriesList.MvpViews.CategoriesFragmentViewImpl;
import com.academy.fundamentals.mymovies.Screens.Common.Presenters.BaseFragment;
import com.academy.fundamentals.mymovies.Screens.FavoritesList.Presenters.FavoritesListFragment;
import com.academy.fundamentals.mymovies.Screens.MoviesList.Presenters.MoviesListFragment;


public class CategoriesFragment extends BaseFragment implements
        CategoriesFragmentView.CategoriesFragmentViewListener {
    private static final String TAG = "CategoriesFragment";

    private CategoriesFragmentViewImpl mViewMvp;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewMvp = new CategoriesFragmentViewImpl(inflater, container);
        mViewMvp.setListener(this);

        return mViewMvp.getRootView();
    }

    @Override
    public void onFavoritesListClick() {
        replaceFragment(FavoritesListFragment.class, true, null);
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