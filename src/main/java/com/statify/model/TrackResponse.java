package com.statify.model;

import java.util.List;

public class TrackResponse {

    private List<Track> items;

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

}
