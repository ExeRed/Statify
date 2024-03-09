package com.statify.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyTopTracksResponse {
    private List<SpotifyTrack> items;

    public List<SpotifyTrack> getItems() {
        return items;
    }

    public void setItems(List<SpotifyTrack> items) {
        this.items = items;
    }
}
