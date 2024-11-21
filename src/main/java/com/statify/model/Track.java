package com.statify.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {
    private String id;
    private String name;
    private List<Artist> artists;
    private Album album;
    private int popularity;
    private int duration_ms;
    private ExternalUrls external_urls;
    private String uri;

    public Track() {
    }

    public Track(String id, String name, List<Artist> artists, Album album, int popularity, int duration_ms, ExternalUrls external_urls, String uri) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.album = album;
        this.popularity = popularity;
        this.duration_ms = duration_ms;
        this.external_urls = external_urls;
        this.uri = uri;
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

    public int getDuration_ms() {
        return duration_ms;
    }

    public void setDuration_ms(int duration_ms) {
        this.duration_ms = duration_ms;
    }

    public ExternalUrls getExternal_urls() {
        return external_urls;
    }

    public void setExternal_urls(ExternalUrls external_urls) {
        this.external_urls = external_urls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Artist> getAllArtists() {
        List<Artist> allArtists = new ArrayList<>();
        if (artists != null) {
            for (Artist artist : artists) {
                if (artist != null && artist.getName() != null) {
                    allArtists.addAll((Collection<? extends Artist>) artist);
                }
            }
        }
        return allArtists;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "TrackDB{" +
                "name='" + name + '\'' +
                ", artists=" + artists +
                ", album=" + album +
                ", popularity=" + popularity +
                '}';
    }
}
