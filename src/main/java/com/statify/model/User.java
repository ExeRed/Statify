package com.statify.model;

public class User {
    private String display_name;

    public User(String display_name) {
        this.display_name = display_name;
    }

    public User() {

    }
    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }
}
