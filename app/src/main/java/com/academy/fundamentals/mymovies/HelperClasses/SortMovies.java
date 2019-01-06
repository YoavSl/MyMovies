package com.academy.fundamentals.mymovies.HelperClasses;

import com.academy.fundamentals.mymovies.Models.Movie;
import com.academy.fundamentals.mymovies.Models.SortType;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SortMovies {

    public static List<Movie> sortByType(List<Movie> movies, SortType sortType, Boolean ascendingSort) {
        switch(sortType) {
            case NAME:
                return sortByName(movies, ascendingSort);
            case RELEASE_DATE:
                return sortByReleaseDate(movies, ascendingSort);
            case RATING:
                return sortByRating(movies, ascendingSort);
            case POPULARITY:
                return sortByPopularity(movies, ascendingSort);
        }
        throw new IllegalStateException("SortMovies, Sort type isn't supported");
    }

    private static List<Movie> sortByName(List<Movie> movies, Boolean ascendingSort) {
        if (ascendingSort) {
            Collections.sort(movies, new Comparator<Movie>() {
                @Override
                public int compare(Movie movie1, Movie movie2) {
                    return movie1.getTitle().compareTo(movie2.getTitle());
                }
            });
        }
        else {
            Collections.sort(movies, new Comparator<Movie>() {
                @Override
                public int compare(Movie movie1, Movie movie2) {
                    return movie2.getTitle().compareTo(movie1.getTitle());
                }
            });
        }
        return movies;
    }

    private static List<Movie> sortByReleaseDate(List<Movie> movies, Boolean ascendingSort) {
        if (ascendingSort) {
            Collections.sort(movies, new Comparator<Movie>() {
                @Override
                public int compare(Movie movie1, Movie movie2) {
                    return movie1.getReleaseDate().compareTo(movie2.getReleaseDate());
                }
            });
        }
        else {
            Collections.sort(movies, new Comparator<Movie>() {
                @Override
                public int compare(Movie movie1, Movie movie2) {
                    return movie2.getReleaseDate().compareTo(movie1.getReleaseDate());
                }
            });
        }
        return movies;
    }

    private static List<Movie> sortByRating(List<Movie> movies, Boolean ascendingSort) {
        if (ascendingSort) {
            Collections.sort(movies, new Comparator<Movie>() {
                @Override
                public int compare(Movie movie1, Movie movie2) {
                    return Float.compare(movie1.getRating(), movie2.getRating());
                }
            });
        }
        else {
            Collections.sort(movies, new Comparator<Movie>() {
                @Override
                public int compare(Movie movie1, Movie movie2) {
                    return Float.compare(movie2.getRating(), movie1.getRating());
                }
            });
        }
        return movies;
    }

    private static List<Movie> sortByPopularity(List<Movie> movies, Boolean ascendingSort) {
        if (ascendingSort) {
            Collections.sort(movies, new Comparator<Movie>() {
                @Override
                public int compare(Movie movie1, Movie movie2) {
                    return Float.compare(movie1.getPopularity(), movie2.getPopularity());
                }
            });
        }
        else {
            Collections.sort(movies, new Comparator<Movie>() {
                @Override
                public int compare(Movie movie1, Movie movie2) {
                    return Float.compare(movie2.getPopularity(), movie1.getPopularity());
                }
            });
        }
        return movies;
    }
}
