package com.statify.repository;

import com.statify.table.PrivacySettingsDB;
import com.statify.table.SpotifyUserDB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivacySettingsRepository extends JpaRepository<PrivacySettingsDB, Long> {
    Optional<PrivacySettingsDB> findByUser(SpotifyUserDB user);
}
