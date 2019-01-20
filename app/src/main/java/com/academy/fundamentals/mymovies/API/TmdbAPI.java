package com.academy.fundamentals.mymovies.API;

import com.academy.fundamentals.mymovies.Models.GenresResponse;
import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Models.MoviesResponse;
import com.academy.fundamentals.mymovies.Models.ReviewsResponse;
import com.academy.fundamentals.mymovies.Models.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface TmdbAPI {

    @GET("search/movie")
    public Call<MoviesResponse> getMoviesByQuery(
            @Query("query") String name,
            @Query("api_key") String key,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{category}")
    public Call<MoviesResponse> getMoviesByCategory(
            @Path("category") String category,
            @Query("api_key") String key,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    public Call<Movie> getMovieById(
            @Path("movie_id") int id,
            @Query("api_key") String key,
            @Query("language") String language
    );

    @GET("genre/movie/list")
    Call<GenresResponse> getGenres(
            @Query("api_key") String key,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/videos")
    Call<TrailersResponse> getTrailers(
            @Path("movie_id") int id,
            @Query("api_key") String key,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/reviews")
    Call<ReviewsResponse> getReviews(
            @Path("movie_id") int id,
            @Query("api_key") String key,
            @Query("language") String language
    );
}