package com.academy.fundamentals.mymovies.Repositories;

import android.content.Context;
import android.support.annotation.NonNull;

import com.academy.fundamentals.mymovies.API.APIClient;
import com.academy.fundamentals.mymovies.API.TmdbAPI;
import com.academy.fundamentals.mymovies.Callbacks.OnGetGenresCallback;
import com.academy.fundamentals.mymovies.Callbacks.OnGetReviewsCallback;
import com.academy.fundamentals.mymovies.Callbacks.OnGetTrailersCallback;
import com.academy.fundamentals.mymovies.Models.GenresResponse;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Models.MoviesResponse;
import com.academy.fundamentals.mymovies.Callbacks.OnGetMovieCallback;
import com.academy.fundamentals.mymovies.Callbacks.OnGetMoviesCallback;
import com.academy.fundamentals.mymovies.Models.ReviewsResponse;
import com.academy.fundamentals.mymovies.Models.TrailersResponse;
import com.academy.fundamentals.mymovies.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MoviesRepository {
    private static final String LANGUAGE = "en-US";

    private static MoviesRepository repository;
    private static APIClient apiClient;

    private TmdbAPI api;

    private MoviesRepository(TmdbAPI api) {
        this.api = api;

        apiClient = new APIClient();
    }

    public static MoviesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = APIClient.getClient();
            repository = new MoviesRepository(retrofit.create(TmdbAPI.class));
        }
        return repository;
    }

    public void getPopularMovies(int page, Context context, final OnGetMoviesCallback callback) {
        api.getMoviesByCategory("popular",
                context.getString(R.string.api_key_tmdb), LANGUAGE, page)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        //progressDoalog.dismiss();
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();

                            if (moviesResponse != null && moviesResponse.getMovies() != null)
                                callback.onSuccess(moviesResponse.getMovies(), moviesResponse.getPage());
                            else
                                callback.onError();
                        }
                        else
                            callback.onError();
                    }

                    @Override
                    public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                        //progressDoalog.dismiss();
                        callback.onError();
                    }
                });
    }

    public void getMovie(int movieId, Context context, final OnGetMovieCallback callback) {
        api.getMovie(movieId, context.getString(R.string.api_key_tmdb), LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie movie = response.body();

                            if (movie != null)
                                callback.onSuccess(movie);
                            else
                                callback.onError();
                        }
                        else
                            callback.onError();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getGenres(Context context, final OnGetGenresCallback callback) {
        api.getGenres(context.getString(R.string.api_key_tmdb), LANGUAGE)
                .enqueue(new Callback<GenresResponse>() {
                    @Override
                    public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                        if (response.isSuccessful()) {
                            GenresResponse genresResponse = response.body();

                            if ((genresResponse != null) && (genresResponse.getGenres() != null))
                                callback.onSuccess(genresResponse.getGenres());
                            else
                                callback.onError();
                        }
                        else
                            callback.onError();
                    }

                    @Override
                    public void onFailure(Call<GenresResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getTrailers(Context context, int movieId, final OnGetTrailersCallback callback) {
        api.getTrailers(movieId, context.getString(R.string.api_key_tmdb), LANGUAGE)
                .enqueue(new Callback<TrailersResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TrailersResponse> call, @NonNull Response<TrailersResponse> response) {
                        if (response.isSuccessful()) {
                            TrailersResponse trailersResponse = response.body();

                            if ((trailersResponse != null) && (trailersResponse.getTrailers() != null))
                                callback.onSuccess(trailersResponse.getTrailers());
                            else
                                callback.onError();
                        }
                        else
                            callback.onError();
                    }

                    @Override
                    public void onFailure(@NonNull Call<TrailersResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getReviews(Context context, int movieId, final OnGetReviewsCallback callback) {
        api.getReviews(movieId, context.getString(R.string.api_key_tmdb), LANGUAGE)
                .enqueue(new Callback<ReviewsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ReviewsResponse> call, @NonNull Response<ReviewsResponse> response) {
                        if (response.isSuccessful()) {
                            ReviewsResponse reviewResponse = response.body();

                            if (reviewResponse != null && reviewResponse.getReviews() != null)
                                callback.onSuccess(reviewResponse.getReviews());
                            else
                                callback.onError();
                        }
                        else
                            callback.onError();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ReviewsResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }
}
