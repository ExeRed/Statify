package com.statify.serviceDB;

import com.statify.model.Track;
import com.statify.model.TrackResponse;
import com.statify.model.User;
import com.statify.repository.SpotifyUserRepository;
import com.statify.service.UserService;
import com.statify.table.SpotifyUserDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpotifyUserService {

    @Autowired
    private SpotifyUserRepository spotifyUserRepository;

    @Autowired
    private UserService userService;

    public SpotifyUserDB findOrCreateUser(User currentUser) {
        return spotifyUserRepository.findById(currentUser.getId())
                .orElseGet(() -> {
                    SpotifyUserDB newUser = new SpotifyUserDB(currentUser.getId(), currentUser.getDisplay_name(), currentUser.getEmail());
                    return spotifyUserRepository.save(newUser);
                });
    }

    public void updateUser(SpotifyUserDB user) {
        user.setLastUpdated(LocalDateTime.now());
        spotifyUserRepository.save(user);

    }

    public SpotifyUserDB getUser(String id) {
        // Найти пользователя по id
        SpotifyUserDB user = spotifyUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Вернуть username
        return user;
    }


    public List<SpotifyUserDB> searchUsers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // Поиск по username с игнорированием регистра
        List<SpotifyUserDB> byUsername = spotifyUserRepository.findByUsernameContainingIgnoreCase(query);

        // Попытка найти по точному ID
        Optional<SpotifyUserDB> byId = spotifyUserRepository.findById(query);

        // Объединение результатов без дубликатов
        Set<SpotifyUserDB> results = new LinkedHashSet<>(byUsername);
        byId.ifPresent(results::add);

        return new ArrayList<>(results);
    }



}
