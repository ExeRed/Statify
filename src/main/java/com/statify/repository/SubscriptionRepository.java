package com.statify.repository;

import com.statify.table.SpotifyUserDB;
import com.statify.table.SubscriptionDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionDB, Long> {

    // Найти подписки пользователя
    List<SubscriptionDB> findByFollower(SpotifyUserDB follower);

    // Найти подписчиков пользователя
    List<SubscriptionDB> findByFollowed(SpotifyUserDB followed);


    SubscriptionDB findByFollowerAndFollowed(SpotifyUserDB follower, SpotifyUserDB followed);

}
