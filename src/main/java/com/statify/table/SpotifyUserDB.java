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

    @Column(name = "custom_id", nullable = false)
    private String customId;

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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PrivacySettingsDB privacySettings;


    // Конструкторы, геттеры и сеттеры
    public SpotifyUserDB() {}

    public SpotifyUserDB(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.lastUpdated = LocalDateTime.now();
        this.customId = id;
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

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
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

    public PrivacySettingsDB getPrivacySettings() {
        return privacySettings;
    }

    public void setPrivacySettings(PrivacySettingsDB privacySettings) {
        this.privacySettings = privacySettings;
    }

    @Override
    public String toString() {
        return "SpotifyUserDB{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
