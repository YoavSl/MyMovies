package com.academy.fundamentals.mymovies.Repositories;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


public class FavoritesRepository {
    private static final String PREFS_NAME = "favorites";
    private static final String FAVORITES_KEY = "favorites_key";
    private static FavoritesRepository repository;
    private SharedPreferences mSharedPrefs;
    private Set<String> favoriteItems;

    private FavoritesRepository(Context context) {
        mSharedPrefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        favoriteItems = getItems();
    }

    public static FavoritesRepository getInstance(Context context) {
        if (repository == null)
            repository = new FavoritesRepository(context.getApplicationContext());

        return repository;
    }

    public static FavoritesRepository getInstance() {
        if (repository != null)
            return repository;

        throw new IllegalArgumentException("Should use getInstance(context) at least once before using this method");
    }

    public Set<String> getItems() {
        return mSharedPrefs.getStringSet(FAVORITES_KEY, favoriteItems);
    }

    private void saveItems() {
        SharedPreferences.Editor mEditor = mSharedPrefs.edit();
        mEditor.putStringSet(FAVORITES_KEY, favoriteItems);
        mEditor.apply();
    }

    public void addItem(int item) {
        favoriteItems.add(String.valueOf(item));
        saveItems();
    }

    public void removeItem(int item) {
        favoriteItems.remove(String.valueOf(item));
        saveItems();
    }

    public Boolean checkIfExists(int item) {
        return favoriteItems.contains(String.valueOf(item));
    }
}
