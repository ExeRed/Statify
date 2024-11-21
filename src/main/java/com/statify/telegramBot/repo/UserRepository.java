package com.statify.telegramBot.repo;

import com.statify.telegramBot.table.UserTelegramId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserTelegramId, Long> {
    Optional<UserTelegramId> findByTelegramUserId(String telegramUserId);
}
