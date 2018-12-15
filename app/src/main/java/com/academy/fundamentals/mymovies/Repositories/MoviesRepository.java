package com.academy.fundamentals.mymovies.Repositories;

import android.content.Context;
import android.support.annotation.NonNull;

import com.academy.fundamentals.mymovies.API.APIClient;
import com.academy.fundamentals.mymovies.API.TmdbAPI;
import com.academy.fundamentals.mymovies.Models.MoviesResponse;
import com.academy.fundamentals.mymovies.OnGetMoviesCallback;
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

    public void getPopularMovies(Context context, int page, final OnGetMoviesCallback callback) {
        api.getMoviesByCategory("popular",
                context.getString(R.string.api_key_tmdb), LANGUAGE, page)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        //progressDoalog.dismiss();
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                callback.onSuccess(moviesResponse.getMovies(), moviesResponse.getPage());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        //progressDoalog.dismiss();
                        callback.onError();
                    }
                });
    }
}
