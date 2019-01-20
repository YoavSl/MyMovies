package com.academy.fundamentals.mymovies.Screens.HomeScreen.MvpViews;

import com.academy.fundamentals.mymovies.Screens.Common.MvpViews.ViewMvp;


public interface HomeScreenFragmentView extends ViewMvp {

    interface HomeScreenFragmentViewListener {
        void onFavoritesListClick();

        void onSearchExecution(String query);

        void onCategoryClick(String category, String apiCategoryName);
    }

    void setListener(HomeScreenFragmentViewListener listener);

    void unbindButterKnife();
}