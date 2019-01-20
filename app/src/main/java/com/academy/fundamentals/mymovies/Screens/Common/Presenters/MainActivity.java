package com.academy.fundamentals.mymovies.Screens.Common.Presenters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.academy.fundamentals.mymovies.R;
import com.academy.fundamentals.mymovies.Screens.HomeScreen.Presenters.HomeScreenFragment;
import com.academy.fundamentals.mymovies.Screens.Common.MvpViews.RootViewMvpImpl;
import com.academy.fundamentals.mymovies.Screens.FavoritesList.Presenters.FavoritesListFragment;
import com.academy.fundamentals.mymovies.Screens.MoviesDetailsList.Presenters.MoviesDetailsListFragment;

import java.util.List;


public class MainActivity extends AppCompatActivity implements BaseFragment.AbstractFragmentCallback {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RootViewMvpImpl mViewMVP = new RootViewMvpImpl(this, null);
        setContentView(mViewMVP.getRootView());

        //Show the default fragment if the application is not restored
        if (savedInstanceState == null)
            replaceFragment(HomeScreenFragment.class, false, null);
    }

    @Override
    public void replaceFragment(Class<? extends Fragment> claz, boolean addToBackStack, Bundle args) {
        Fragment newFragment;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        try {
            newFragment = claz.newInstance();
            if (args != null) newFragment.setArguments(args);
        } catch (InstantiationException e) {
            e.printStackTrace();
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        if (addToBackStack)
            ft.addToBackStack(null);

        if (newFragment instanceof MoviesDetailsListFragment)
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
        else if (!(newFragment instanceof HomeScreenFragment))
            ft.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left, R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);

        if ((newFragment instanceof FavoritesListFragment) || (newFragment instanceof MoviesDetailsListFragment)) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFL);

            if (currentFragment != null) {
                ft.hide(currentFragment);
                ft.add(R.id.fragmentContainerFL, newFragment, claz.getSimpleName());
            }
        }
        else
            ft.replace(R.id.fragmentContainerFL, newFragment, claz.getSimpleName());

        ft.commit();
    }
}