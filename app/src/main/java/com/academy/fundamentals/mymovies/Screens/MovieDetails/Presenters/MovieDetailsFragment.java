package com.academy.fundamentals.mymovies.Screens.MovieDetails.Presenters;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.Screens.Common.Presenters.BaseFragment;
import com.academy.fundamentals.mymovies.Screens.MovieDetails.MvpViews.MovieDetailsFragmentView;
import com.academy.fundamentals.mymovies.Screens.MovieDetails.MvpViews.MovieDetailsFragmentViewImpl;


public class MovieDetailsFragment extends BaseFragment implements
        MovieDetailsFragmentView.MovieDetailsFragmentViewListener{
    private static final String TAG = "MovieDetailsFragment";
    public static final String ARG_MOVIE_ID = "arg_movie_id";
    private MovieDetailsFragmentViewImpl mViewMvp;
    private int movieId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*Bundle args = getArguments();
        if ((args != null) && (args.containsKey(ARG_MOVIE_ID)))
            movieId = args.getInt(ARG_MOVIE_ID);
        else
            throw new IllegalStateException("MovieDetailsFragment must be started with movie ID argument");
*/
        mViewMvp = new MovieDetailsFragmentViewImpl(inflater, container);
        mViewMvp.setListener(this);

        mViewMvp.bindMovieDetails(null);

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
