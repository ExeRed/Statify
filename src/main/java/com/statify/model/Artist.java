package com.statify.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist {
    private String name;
    private int popularity;
    private List<Images> images;
    private List<Genres> genres;
    private Followers followers;
    private String id;
    private ExternalUrls external_urls;

    public Artist(String name, int popularity, List<Images> images, List<Genres> genres, Followers followers, String id, ExternalUrls external_urls) {
        this.name = name;
        this.popularity = popularity;
        this.images = images;
        this.genres = genres;
        this.followers = followers;
        this.id = id;
        this.external_urls = external_urls;
    }

    public Artist() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    private String formattedFollowers;  // Для хранения отформатированного числа

    public List<String> getAllGenres() {
        List<String> allGenres = new ArrayList<>();
        if (genres != null) {
            for (Genres genreList : genres) {
                if (genreList != null && genreList.getGenres() != null) {
                    allGenres.addAll(genreList.getGenres());
                }
            }
        }
        return allGenres;
    }

    public List<Genres> getGenres() {
        return genres;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }

    public Followers getFollowers() {
        return followers;
    }

    public void setFollowers(Followers followers) {
        this.followers = followers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExternalUrls getExternal_urls() {
        return external_urls;
    }

    public void setExternal_urls(ExternalUrls external_urls) {
        this.external_urls = external_urls;
    }

    public String getFormattedFollowers() {
        return formattedFollowers;
    }

    public void setFormattedFollowers(String formattedFollowers) {
        this.formattedFollowers = formattedFollowers;
    }
}

