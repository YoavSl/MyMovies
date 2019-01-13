package com.academy.fundamentals.mymovies.Screens.CategoriesList.MvpViews;

import android.content.Context;
import android.os.Bundle;
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


public class CategoriesFragmentViewImpl implements CategoriesFragmentView {
    private static final String TAG = "CategoriesFtViewImpl";

    private View mRootView;
    private Unbinder unbinder;
    private CategoriesFragmentViewListener mListener;

    @BindView(R.id.toolbar) Toolbar toolbar;

    public CategoriesFragmentViewImpl(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_categories, container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        confToolbar();
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
    public void setListener(CategoriesFragmentViewListener listener) {
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