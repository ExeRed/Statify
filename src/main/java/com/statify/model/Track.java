package com.statify.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {
    private String name;
    private List<Artist> artists;
    private Album album;
    private int popularity;

    public Track() {
    }

    public Track(String name, List<Artist> artists, Album album, int popularity) {
        this.name = name;
        this.artists = artists;
        this.album = album;
        this.popularity = popularity;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }


    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    @JsonProperty("popularity")
    public int getPopularity() {
        return popularity;
    }

    @JsonProperty("popularity")
    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return "Track{" +
                "name='" + name + '\'' +
                ", artists=" + artists +
                ", album=" + album +
                ", popularity=" + popularity +
                '}';
    }
}
