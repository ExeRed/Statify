package com.statify.table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "spotify_tokens")
public class SpotifyToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @JsonIgnore
    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @JsonIgnore
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    // Связь с пользователем One-to-One
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private SpotifyUserDB user;

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public SpotifyUserDB getUser() {
        return user;
    }

    public void setUser(SpotifyUserDB user) {
        this.user = user;
    }
}
