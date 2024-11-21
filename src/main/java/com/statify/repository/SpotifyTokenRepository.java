package com.statify.repository;

import com.statify.table.SpotifyToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotifyTokenRepository extends JpaRepository<SpotifyToken, Long> {
    SpotifyToken findByUserId(String userId);
}
