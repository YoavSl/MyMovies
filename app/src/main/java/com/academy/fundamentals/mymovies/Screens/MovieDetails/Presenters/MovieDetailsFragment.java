package com.academy.fundamentals.mymovies.Screens.MovieDetails.Presenters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academy.fundamentals.mymovies.Callbacks.OnGetReviewsCallback;
import com.academy.fundamentals.mymovies.Callbacks.OnGetTrailersCallback;
import com.academy.fundamentals.mymovies.Models.Genre;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Models.Review;
import com.academy.fundamentals.mymovies.Models.Trailer;
import com.academy.fundamentals.mymovies.Repositories.FavoritesRepository;
import com.academy.fundamentals.mymovies.Repositories.MoviesRepository;
import com.academy.fundamentals.mymovies.Screens.Common.Presenters.BaseFragment;
import com.academy.fundamentals.mymovies.Screens.MovieDetails.MvpViews.MovieDetailsFragmentView;
import com.academy.fundamentals.mymovies.Screens.MovieDetails.MvpViews.MovieDetailsFragmentViewImpl;

import java.util.List;

import static com.academy.fundamentals.mymovies.Screens.FavoritesList.Presenters.FavoritesListFragment.ACTION_REMOVE_FAVORITE_MOVIE;


public class MovieDetailsFragment extends BaseFragment implements
        MovieDetailsFragmentView.MovieDetailsFragmentViewListener{
    private static final String TAG = "MovieDetailsFragment";
    public static final String ARG_MOVIE = "arg_movie";
    public static final String ARG_MOVIE_ID = "arg_movie_id";
    public static final String ARG_GENRES = "arg_genres";


    private MovieDetailsFragmentViewImpl mViewMvp;
    private FavoritesRepository favoritesRepository;
    private MoviesRepository moviesRepository;
    private Movie movie;
    private List<Genre> genres;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if ((args != null) && (args.containsKey(ARG_MOVIE)) && (args.containsKey(ARG_GENRES))) {
            movie = (Movie) args.getSerializable(ARG_MOVIE);
            genres = (List<Genre>) args.getSerializable(ARG_GENRES);
        }
        else
            throw new IllegalStateException("MovieDetailsFragment must be started with a Movie object and Genre array");

        mViewMvp = new MovieDetailsFragmentViewImpl(inflater, container);
        init(inflater.getContext());

        return mViewMvp.getRootView();
    }

    private void init(Context context) {
        mViewMvp.setListener(this);

        favoritesRepository = FavoritesRepository.getInstance(context);
        moviesRepository = MoviesRepository.getInstance();

        mViewMvp.bindMovieDetails(movie, genres,
                checkIfInFavorites(movie.getId()));

        getTrailers(movie);
        getReviews(movie);
    }

    private void getTrailers(Movie movie) {
        moviesRepository.getTrailers(movie.getId(), new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<Trailer> trailers) {
                mViewMvp.displayTrailers(trailers);
            }

            @Override
            public void onError() {
                mViewMvp.getTrailersFailed();
            }
        });
    }

    private void getReviews(Movie movie) {
        moviesRepository.getReviews(movie.getId(), new OnGetReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews) {
                mViewMvp.displayReviews(reviews);
            }

            @Override
            public void onError() {
                mViewMvp.getReviewsFailed();
            }
        });
    }

    @Override
    public void onTrailerClick(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onFavoriteFabClick() {
        int movieId = movie.getId();

        if (checkIfInFavorites(movieId)) {
            favoritesRepository.removeItem(movieId);
            mViewMvp.setFavoriteFab(false);

            Intent intent = new Intent();
            intent.setAction(ACTION_REMOVE_FAVORITE_MOVIE);
            intent.putExtra(ARG_MOVIE_ID, movieId);

            LocalBroadcastManager.getInstance(mViewMvp.getRootView().getContext()).
                    sendBroadcast(intent);
        }
        else {
            favoritesRepository.addItem(movieId);
            mViewMvp.setFavoriteFab(true);
        }
    }

    private boolean checkIfInFavorites(int movieId) {
        return favoritesRepository.checkIfExists(movieId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewMvp.unbindButterKnife();
    }
}