package com.statify.model;

public class PlayedTrack {
    private Track track;
    private String played_at;

    public PlayedTrack() {}

    public PlayedTrack(Track track, String played_at) {
        this.track = track;
        this.played_at = played_at;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public String getPlayed_at() {
        return played_at;
    }

    public void setPlayed_at(String played_at) {
        this.played_at = played_at;
    }
}
