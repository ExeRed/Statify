package com.statify.repository;

import com.statify.table.SpotifyUserDB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpotifyUserRepository extends JpaRepository<SpotifyUserDB, String> {
    SpotifyUserDB findByEmail(String email);

    // Поиск по имени пользователя (с поддержкой частичного совпадения)
    List<SpotifyUserDB> findByUsernameContainingIgnoreCase(String query);

    // Поиск по ID (точное совпадение)
    Optional<SpotifyUserDB> findByCustomId(String id);
}
