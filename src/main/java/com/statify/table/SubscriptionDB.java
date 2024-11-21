package com.statify.table;

import javax.persistence.*;

@Entity
@Table(name = "subscriptions")
public class SubscriptionDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Пользователь, который подписывается
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private SpotifyUserDB follower;

    // Пользователь, на которого подписываются
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_id")
    private SpotifyUserDB followed;

    public SubscriptionDB() {}

    public SubscriptionDB(SpotifyUserDB follower, SpotifyUserDB followed) {
        this.follower = follower;
        this.followed = followed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SpotifyUserDB getFollower() {
        return follower;
    }

    public void setFollower(SpotifyUserDB follower) {
        this.follower = follower;
    }

    public SpotifyUserDB getFollowed() {
        return followed;
    }

    public void setFollowed(SpotifyUserDB followed) {
        this.followed = followed;
    }
}
