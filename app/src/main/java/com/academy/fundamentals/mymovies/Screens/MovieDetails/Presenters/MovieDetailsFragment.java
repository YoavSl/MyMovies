package com.academy.fundamentals.mymovies.Screens.MovieDetails.Presenters;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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


public class MovieDetailsFragment extends BaseFragment implements
        MovieDetailsFragmentView.MovieDetailsFragmentViewListener{
    private static final String TAG = "MovieDetailsFragment";
    public static final String ARG_MOVIE = "arg_movie";
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
        if ((args != null) && (args.containsKey(ARG_MOVIE))) {
            movie = (Movie) args.getSerializable(ARG_MOVIE);
            genres = (List<Genre>) args.getSerializable(ARG_GENRES);
        }
        else
            throw new IllegalStateException("MovieDetailsFragment must be started with a Movie object and Genre array");

        mViewMvp = new MovieDetailsFragmentViewImpl(inflater, container);
        mViewMvp.setListener(this);

        favoritesRepository = FavoritesRepository.getInstance(inflater.getContext());

        mViewMvp.bindMovieDetails(movie, genres,
                checkIfInFavorites());

        moviesRepository = MoviesRepository.getInstance();
        getTrailers(movie);
        getReviews(movie);

        return mViewMvp.getRootView();
    }

    private void getTrailers(Movie movie) {
        moviesRepository.getTrailers(getContext(), movie.getId(), new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<Trailer> trailers) {
                mViewMvp.displayTrailers(trailers);
            }

            @Override
            public void onError() {
            }
        });
    }

    private void getReviews(Movie movie) {
        moviesRepository.getReviews(getContext(), movie.getId(), new OnGetReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews) {
                mViewMvp.displayReviews(reviews);
            }

            @Override
            public void onError() {
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
        if (checkIfInFavorites()) {
            favoritesRepository.removeItem(movie.getId());
            mViewMvp.setFavoriteFab(false);
        }
        else {
            favoritesRepository.addItem(movie.getId());
            mViewMvp.setFavoriteFab(true);
        }
    }

    private boolean checkIfInFavorites() {
        return favoritesRepository.checkIfExists(movie.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewMvp.unbindButterKnife();
    }
}
