package com.statify.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyTrack {
    private String name;
    private List<SpotifyArtist> artists;
    private int popularity;
    private List<SpotifyImage> images;

    // Getters and setters
}

@JsonIgnoreProperties(ignoreUnknown = true)
class SpotifyArtist {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class SpotifyImage {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
