package com.academy.fundamentals.mymovies.Screens.Common.MvpViews;

import android.os.Bundle;
import android.view.View;


public interface ViewMvp {

    /** @return root Android View of this MVP View */
    public View getRootView();

     /** @return Bundle containing the state of this MVC View, or null if the view has no state */
    public Bundle getViewState();
}
