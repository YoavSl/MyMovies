package com.academy.fundamentals.mymovies.Screens.MovieDetails.Presenters;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Screens.Common.Presenters.BaseFragment;
import com.academy.fundamentals.mymovies.Screens.MovieDetails.MvpViews.MovieDetailsFragmentView;
import com.academy.fundamentals.mymovies.Screens.MovieDetails.MvpViews.MovieDetailsFragmentViewImpl;


public class MovieDetailsFragment extends BaseFragment implements
        MovieDetailsFragmentView.MovieDetailsFragmentViewListener{
    private static final String TAG = "MovieDetailsFragment";
    public static final String ARG_MOVIE = "arg_movie";

    private MovieDetailsFragmentViewImpl mViewMvp;
    private Movie movie;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if ((args != null) && (args.containsKey(ARG_MOVIE)))
            movie = (Movie) args.getSerializable(ARG_MOVIE);
        else
            throw new IllegalStateException("MovieDetailsFragment must be started with a Movie object");

        mViewMvp = new MovieDetailsFragmentViewImpl(inflater, container);
        mViewMvp.setListener(this);

        mViewMvp.bindMovieDetails(movie);

        return mViewMvp.getRootView();
    }

    @Override
    public void onTrailerClick() {
        Intent ytIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=dxWvtMOGAhw"));
        startActivity(ytIntent);
        //Log.e(TAG, "myCheck onTrailerClick" + movieId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewMvp.unbindButterKnife();
    }
}
