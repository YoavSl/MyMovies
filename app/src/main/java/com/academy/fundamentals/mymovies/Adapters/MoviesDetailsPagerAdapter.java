package com.academy.fundamentals.mymovies.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.academy.fundamentals.mymovies.Screens.MovieDetails.Presenters.MovieDetailsFragment;


public class MoviesDetailsPagerAdapter extends FragmentStatePagerAdapter {
    private static final int MOVIES_COUNT = 5;

    public MoviesDetailsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return MOVIES_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return new MovieDetailsFragment();
    }
}