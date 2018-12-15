package com.academy.fundamentals.mymovies.Screens.MovieDetails.MvpViews;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MovieDetailsFragmentViewImpl implements MovieDetailsFragmentView {
    private static final String TAG = "MovieDetailsFtViewImpl";
    private View mRootView;
    private Unbinder unbinder;
    private MovieDetailsFragmentViewListener mListener;

    @BindView(R.id.movieTitleTV) TextView movieTitleTV;
    @BindView(R.id.movieOverviewTV) TextView movieOverviewTV;
    @BindView(R.id.movieCoverIV) ImageView movieCoverIV;
    @BindView(R.id.trailerBT) FloatingActionButton trailerBT;

    public MovieDetailsFragmentViewImpl(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
    }

    @OnClick(R.id.trailerBT)
    void OnClick(View view) {
        if (mListener != null) {
            mListener.onTrailerClick();
        }
    }

    @Override
    public void bindMovieDetails(Movie movie) {
        //movieTitleTV.setText(movie.getTitle());
        //movieOverviewTV.setText(movie.getOverview());
        //movieCoverIV.setImageResource(movie.getCover());

        movieTitleTV.setText("Black Panther");
        movieOverviewTV.setText("King T'Challa returns home from America to the reclusive, technologically advanced African nation of Wakanda to serve as his country's new leader. However, T'Challa soon finds that he is challenged for the throne by factions within his own country as well as without. Using powers reserved to Wakandan kings, T'Challa assumes the Black Panther mantel to join with girlfriend Nakia, the queen-mother, his princess-kid sister, members of the Dora Milaje (the Wakandan 'special forces') and an American secret agent, to prevent Wakanda from being dragged into a world war.");
    }

    @Override
    public void setListener(MovieDetailsFragmentViewListener listener) {
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
