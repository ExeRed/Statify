package com.statify.model;

import java.util.List;

public class TrackResponse {

    private List<Track> items;
    private List<Track> tracks;

    private Track item;

    public Track getItem() {
        return item;
    }

    public void setItem(Track item) {
        this.item = item;
    }

    public List<Track> getItems() {
        return items;
    }

    public void setItems(List<Track> items) {
        this.items = items;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

}
