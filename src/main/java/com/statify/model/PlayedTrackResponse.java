package com.statify.model;

import java.util.List;

public class PlayedTrackResponse {

    private List<PlayedTrack> items;

    public List<PlayedTrack> getItems() {
        return items;
    }

    public void setItems(List<PlayedTrack> items) {
        this.items = items;
    }
}