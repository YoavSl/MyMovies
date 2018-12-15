package com.academy.fundamentals.mymovies.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Screens.MovieDetails.Presenters.MovieDetailsFragment;

import java.util.List;


public class MoviesDetailsPagerAdapter extends FragmentStatePagerAdapter {
    private List<Movie> movies;

    public MoviesDetailsPagerAdapter(FragmentManager fragmentManager, List<Movie> movies) {
        super(fragmentManager);

        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MovieDetailsFragment.ARG_MOVIE, movies.get(position));

        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        movieDetailsFragment.setArguments(bundle);

        return movieDetailsFragment;
    }
}