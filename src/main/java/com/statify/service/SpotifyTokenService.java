package com.statify.service;
import com.statify.repository.SpotifyTokenRepository;
import com.statify.repository.SpotifyUserRepository;
import com.statify.table.SpotifyToken;
import com.statify.table.SpotifyUserDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;
import java.time.LocalDateTime;

@Service
public class SpotifyTokenService {

    @Autowired
    private SpotifyTokenRepository spotifyTokenRepository;

    @Autowired
    private SpotifyUserRepository spotifyUserRepository;

    public String refreshAccessToken(String userId) {
        // Получаем refresh token из базы
        SpotifyToken tokenInfo = spotifyTokenRepository.findByUserId(userId);

        if (tokenInfo == null) {
            throw new RuntimeException("No token found for user");
        }

        // Проверяем, истек ли токен
        if (tokenInfo.getExpiresAt().isAfter(LocalDateTime.now())) {
            return tokenInfo.getAccessToken();
        }

        // Параметры для запроса обновления токена
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", tokenInfo.getRefreshToken());
        params.add("client_id", "f9e2e07d692d4ee4a1caa1343269537a");
        params.add("client_secret", "6d671a1c563e4a64ad46d2a6138c3c65");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    "https://accounts.spotify.com/api/token",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> responseBody = response.getBody();
            String newAccessToken = (String) responseBody.get("access_token");
            int expiresIn = (int) responseBody.get("expires_in");

            // Обновляем токен в базе данных
            tokenInfo.setAccessToken(newAccessToken);
            tokenInfo.setExpiresAt(LocalDateTime.now().plusSeconds(expiresIn));
            spotifyTokenRepository.save(tokenInfo);

            return newAccessToken;
        } catch (Exception e) {
            throw new RuntimeException("Failed to refresh token: " + e.getMessage());
        }
    }

    public void saveTokens(String userId, String accessToken, String refreshToken, LocalDateTime expiresAt) {
        SpotifyUserDB user = spotifyUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Проверяем, есть ли уже сохранённые токены для этого пользователя
        SpotifyToken existingToken = spotifyTokenRepository.findByUserId(userId);

        if (existingToken == null) {
            // Если токены ещё не сохранены, создаём новые
            existingToken = new SpotifyToken();
            existingToken.setUser(user);
        }

        // Обновляем данные токенов
        existingToken.setAccessToken(accessToken);
        existingToken.setRefreshToken(refreshToken);
        existingToken.setExpiresAt(expiresAt);

        // Сохраняем или обновляем запись
        spotifyTokenRepository.save(existingToken);
    }

}
