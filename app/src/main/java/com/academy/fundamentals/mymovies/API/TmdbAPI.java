package com.academy.fundamentals.mymovies.API;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Models.MoviesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface TmdbAPI {

    @GET("movie/{category}")
    public Call<MoviesResponse> getMoviesByCategory(
            @Path("category") String category,
            @Query("api_key") String key,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{id}")
    public Call<Movie> getMovie(
            @Path("id") long id,
            @Query("api_key") String key,
            @Query("language") String language
    );

    @GET("search/movie")
    public Call<Movie> getMovieByName(
            @Query("query") String name,
            @Query("api_key") String key
    );
}
