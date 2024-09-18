package com.statify.repository;

import com.statify.table.SpotifyUserDB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpotifyUserRepository extends JpaRepository<SpotifyUserDB, String> {
    SpotifyUserDB findByEmail(String email);

    // Поиск по никнейму с частичным совпадением
    List<SpotifyUserDB> findByUsernameContaining(String username);
}
