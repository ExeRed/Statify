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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        // Search for users by username (partial matches) and by exact ID
        List<SpotifyUserDB> usersByUsername = spotifyUserRepository.findByUsernameContaining(query);
        Optional<SpotifyUserDB> userById = spotifyUserRepository.findById(query);

        // Prepare the result list
        List<SpotifyUserDB> result = new ArrayList<>();

        // If the user by ID exists, add it to the result
        userById.ifPresent(result::add);

        // Add all users that match the username query
        result.addAll(usersByUsername);

        return result;
    }


}
