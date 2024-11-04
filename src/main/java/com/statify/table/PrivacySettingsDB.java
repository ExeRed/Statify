package com.statify.table;

import javax.persistence.*;

@Entity
@Table(name = "privacy_settings")
public class PrivacySettingsDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ссылка на пользователя (One-to-One связь)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SpotifyUserDB user;

    @Column(name = "show_followers", nullable = false)
    private boolean showFollowers = true;

    @Column(name = "show_following", nullable = false)
    private boolean showFollowing = true;

    @Column(name = "show_top_artists", nullable = false)
    private boolean showTopArtists = true;

    @Column(name = "show_top_tracks", nullable = false)
    private boolean showTopTracks = true;

    @Column(name = "show_recently_played", nullable = false)
    private boolean showRecentlyPlayed = true;

    public PrivacySettingsDB() {
    }

    public PrivacySettingsDB(SpotifyUserDB user, boolean showFollowers, boolean showFollowing, boolean showTopArtists, boolean showTopTracks, boolean showRecentlyPlayed) {
        this.user = user;
        this.showFollowers = showFollowers;
        this.showFollowing = showFollowing;
        this.showTopArtists = showTopArtists;
        this.showTopTracks = showTopTracks;
        this.showRecentlyPlayed = showRecentlyPlayed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SpotifyUserDB getUser() {
        return user;
    }

    public void setUser(SpotifyUserDB user) {
        this.user = user;
    }

    public boolean isShowFollowers() {
        return showFollowers;
    }

    public void setShowFollowers(boolean showFollowers) {
        this.showFollowers = showFollowers;
    }

    public boolean isShowFollowing() {
        return showFollowing;
    }

    public void setShowFollowing(boolean showFollowing) {
        this.showFollowing = showFollowing;
    }

    public boolean isShowTopArtists() {
        return showTopArtists;
    }

    public void setShowTopArtists(boolean showTopArtists) {
        this.showTopArtists = showTopArtists;
    }

    public boolean isShowTopTracks() {
        return showTopTracks;
    }

    public void setShowTopTracks(boolean showTopTracks) {
        this.showTopTracks = showTopTracks;
    }

    public boolean isShowRecentlyPlayed() {
        return showRecentlyPlayed;
    }

    public void setShowRecentlyPlayed(boolean showRecentlyPlayed) {
        this.showRecentlyPlayed = showRecentlyPlayed;
    }

    @Override
    public String toString() {
        return "PrivacySettingsDB{" +
                "id=" + id +
                ", user=" + user +
                ", showFollowers=" + showFollowers +
                ", showFollowing=" + showFollowing +
                ", showTopArtists=" + showTopArtists +
                ", showTopTracks=" + showTopTracks +
                ", showRecentlyPlayed=" + showRecentlyPlayed +
                '}';
    }
}
