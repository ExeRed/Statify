package com.statify.model;

public class User {
    private String display_name;
    private String id;
    private ExternalUrls external_urls;

    public User(String display_name, String id, ExternalUrls external_urls) {
        this.display_name = display_name;
        this.id = id;
        this.external_urls = external_urls;
    }

    public User() {

    }
    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
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
}
