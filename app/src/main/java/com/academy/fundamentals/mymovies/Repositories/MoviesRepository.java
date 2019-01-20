package com.academy.fundamentals.mymovies.Repositories;

import android.support.annotation.NonNull;

import com.academy.fundamentals.mymovies.API.APIClient;
import com.academy.fundamentals.mymovies.API.TmdbAPI;
import com.academy.fundamentals.mymovies.Callbacks.OnGetGenresCallback;
import com.academy.fundamentals.mymovies.Callbacks.OnGetReviewsCallback;
import com.academy.fundamentals.mymovies.Callbacks.OnGetTrailersCallback;
import com.academy.fundamentals.mymovies.Models.Genre;
import com.academy.fundamentals.mymovies.Models.GenresResponse;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Models.MoviesResponse;
import com.academy.fundamentals.mymovies.Callbacks.OnGetMovieCallback;
import com.academy.fundamentals.mymovies.Callbacks.OnGetMoviesCallback;
import com.academy.fundamentals.mymovies.Models.ReviewsResponse;
import com.academy.fundamentals.mymovies.Models.TrailersResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MoviesRepository {
    private static final String API_KEY = "e46fac10656466cafbc9ee4d988cdcb1";
    private static final String LANGUAGE = "en-US";

    private static MoviesRepository repository;
    private static APIClient apiClient;
    private List<Genre> genres;
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

    public void getMoviesByQuery(String query, int page, final OnGetMoviesCallback callback) {
        api.getMoviesByQuery(query, API_KEY, LANGUAGE, page)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();

                            if (moviesResponse != null && moviesResponse.getMovies() != null)
                                callback.onSuccess(moviesResponse.getMovies(), moviesResponse.getPage());
                            else
                                callback.onError();
                        } else
                            callback.onError();
                    }

                    @Override
                    public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getMoviesByCategory(String apiCategoryName, int page, final OnGetMoviesCallback callback) {
        api.getMoviesByCategory(apiCategoryName, API_KEY, LANGUAGE, page)
            .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
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
                        callback.onError();
                    }
                });
    }

    public void getMovieById(int movieId, final OnGetMovieCallback callback) {
        api.getMovieById(movieId, API_KEY, LANGUAGE)
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

    public void getGenres(final OnGetGenresCallback callback) {
        api.getGenres(API_KEY, LANGUAGE)
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

    public void getTrailers(int movieId, final OnGetTrailersCallback callback) {
        api.getTrailers(movieId, API_KEY, LANGUAGE)
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

    public void getReviews(int movieId, final OnGetReviewsCallback callback) {
        api.getReviews(movieId, API_KEY, LANGUAGE)
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

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
