package com.statify.model;

import java.util.List;

public class Playlist {
    private List<Images> images;
    private String name;

    public Playlist() {}

    public Playlist(List<Images> images, String name) {
        this.images = images;
        this.name = name;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
