package com.academy.fundamentals.mymovies.Screens.CategoriesList.MvpViews;

import com.academy.fundamentals.mymovies.Screens.Common.MvpViews.ViewMvp;


public interface CategoriesFragmentView extends ViewMvp {

    interface CategoriesFragmentViewListener {
        void onFavoritesListClick();

        void onCategoryClick(String category, String apiCategoryName);
    }

    void setListener(CategoriesFragmentViewListener listener);

    void unbindButterKnife();
}