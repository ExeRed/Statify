package com.statify.service;

import com.statify.table.PrivacySettingsDB;
import com.statify.table.SpotifyUserDB;
import com.statify.repository.PrivacySettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivacySettingsService {

    @Autowired
    private PrivacySettingsRepository privacySettingsRepository;

    // Получение настроек приватности пользователя
    public PrivacySettingsDB getPrivacySettingsByUser(SpotifyUserDB user) {
        return privacySettingsRepository.findByUser(user)
                .orElseGet(() -> createDefaultSettings(user));
    }

    // Сохранение настроек приватности
    public void savePrivacySettings(SpotifyUserDB user, PrivacySettingsDB privacySettings) {
        privacySettings.setUser(user);
        privacySettingsRepository.save(privacySettings);
    }

    // Создание настроек приватности по умолчанию для нового пользователя
    private PrivacySettingsDB createDefaultSettings(SpotifyUserDB user) {
        PrivacySettingsDB defaultSettings = new PrivacySettingsDB();
        defaultSettings.setUser(user);
        defaultSettings.setShowFollowers(true);
        defaultSettings.setShowFollowing(true);
        defaultSettings.setShowTopArtists(true);
        defaultSettings.setShowTopTracks(true);
        defaultSettings.setShowRecentlyPlayed(true);
        return privacySettingsRepository.save(defaultSettings);
    }
}
