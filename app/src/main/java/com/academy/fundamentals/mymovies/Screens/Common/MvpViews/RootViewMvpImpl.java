package com.academy.fundamentals.mymovies.Screens.Common.MvpViews;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.R;


public class RootViewMvpImpl implements ViewMvp {
    private static final String TAG = "RootViewMvpImpl";

    private View mRootView;

    public RootViewMvpImpl(Context context, ViewGroup container) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.mvp_root_view, container);
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;   // This MVP view has no state that could be retrieved
    }
}
