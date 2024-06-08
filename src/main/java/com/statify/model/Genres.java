package com.statify.model;

import java.util.ArrayList;
import java.util.List;

public class Genres {
    private List<String> genres;

    public Genres() {}

    public Genres(List<String> genres) {
        this.genres = genres;
    }

    public Genres(String genre) {
        this.genres = new ArrayList<>();
        this.genres.add(genre); // Add the single genre to the list
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
