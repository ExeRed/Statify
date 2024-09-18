package com.statify.serviceDB;


import com.statify.table.SpotifyUserDB;
import com.statify.table.SubscriptionDB;
import com.statify.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    // Добавить подписку
    public boolean followUser(SpotifyUserDB follower, SpotifyUserDB followed) {
        if (!isAlreadyFollowing(follower, followed)) {
            SubscriptionDB subscription = new SubscriptionDB(follower, followed);
            subscriptionRepository.save(subscription);
            return true;
        }
        return false;
    }

    // Удалить подписку
    public boolean unfollowUser(SpotifyUserDB follower, SpotifyUserDB followed) {
        SubscriptionDB subscription = subscriptionRepository.findByFollowerAndFollowed(follower, followed);
        if (subscription != null) {
            subscriptionRepository.delete(subscription);
            return true;
        }
        return false;
    }

    // Проверка, подписан ли пользователь
    public boolean isAlreadyFollowing(SpotifyUserDB follower, SpotifyUserDB followed) {
        return subscriptionRepository.findByFollowerAndFollowed(follower, followed) != null;
    }

    // Получить список подписчиков
    public List<SubscriptionDB> getFollowers(SpotifyUserDB user) {
        return subscriptionRepository.findByFollowed(user);
    }

    // Получить список, на кого подписан пользователь
    public List<SubscriptionDB> getFollowing(SpotifyUserDB user) {
        return subscriptionRepository.findByFollower(user);
    }
}
