package com.statify.service;

import com.statify.repository.SpotifyUserRepository;
import com.statify.table.PrivacySettingsDB;
import com.statify.table.SpotifyUserDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class PrivacySettingsInitializationService {

    @Autowired
    private SpotifyUserRepository spotifyUserRepository;

    @Autowired
    private PrivacySettingsService privacySettingsService;

    // Этот метод выполнится при запуске приложения
    @PostConstruct
    public void initializePrivacySettingsForAllUsers() {
        List<SpotifyUserDB> users = spotifyUserRepository.findAll();
        for (SpotifyUserDB user : users) {
            privacySettingsService.getPrivacySettingsByUser(user); // Создаст настройки по умолчанию, если их нет
        }
    }
}
