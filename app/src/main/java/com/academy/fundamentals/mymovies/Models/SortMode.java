package com.academy.fundamentals.mymovies.Models;


public class SortMode {
    private String name;
    private SortType type;
    private boolean defaultAscendSort;

    public SortMode(String name, SortType type, boolean defaultAscendSort) {
        this.name = name;
        this.type = type;
        this.defaultAscendSort = defaultAscendSort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SortType getType() {
        return type;
    }

    public void setType(SortType type) {
        this.type = type;
    }

    public boolean isDefaultAscendSort() {
        return defaultAscendSort;
    }

    public void setDefaultAscendSort(boolean defaultAscendSort) {
        this.defaultAscendSort = defaultAscendSort;
    }
}
