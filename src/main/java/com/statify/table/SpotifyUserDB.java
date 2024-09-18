package com.statify.table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "spotify_users")
public class SpotifyUserDB {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SpotifyToken spotifyToken;

    // Связь с подписками
    @JsonIgnore
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubscriptionDB> following; // Список пользователей, на кого подписан

    @JsonIgnore
    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubscriptionDB> followers; // Список подписчиков

    // Конструкторы, геттеры и сеттеры
    public SpotifyUserDB() {}

    public SpotifyUserDB(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.lastUpdated = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public SpotifyToken getSpotifyToken() {
        return spotifyToken;
    }

    public void setSpotifyToken(SpotifyToken spotifyToken) {
        this.spotifyToken = spotifyToken;
    }

    public List<SubscriptionDB> getFollowing() {
        return following;
    }

    public void setFollowing(List<SubscriptionDB> following) {
        this.following = following;
    }

    public List<SubscriptionDB> getFollowers() {
        return followers;
    }

    public void setFollowers(List<SubscriptionDB> followers) {
        this.followers = followers;
    }
}
