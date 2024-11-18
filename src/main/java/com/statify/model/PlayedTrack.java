package com.statify.model;


import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class PlayedTrack {
    private Track track;
    private String played_at;

    public PlayedTrack() {}

    public PlayedTrack(Track track, String played_at) {
        this.track = track;
        this.played_at = played_at;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public String getPlayed_at() {
        return played_at;
    }

    public void setPlayed_at(String played_at) {
        this.played_at = played_at;
    }

    // Метод для получения времени, прошедшего с момента проигрывания
    public String getTimeAgo() {
        try {
            // Преобразуем строку в Instant
            Instant playedAtInstant = Instant.parse(played_at);
            Instant now = Instant.now();

            // Считаем разницу в миллисекундах
            long diffInMillis = Duration.between(playedAtInstant, now).toMillis();

            // Переводим в секунды и минуты
            long secondsAgo = diffInMillis / 1000;
            long minutesAgo = secondsAgo / 60;

            // Формируем строку
            if (secondsAgo < 60) {
                return secondsAgo + "s"; // Если прошло меньше минуты
            } else if (minutesAgo < 60) {
                return minutesAgo + "m";
            } else if (minutesAgo < 1440) {
                long hoursAgo = minutesAgo / 60;
                return hoursAgo + "h";
            } else {
                long daysAgo = minutesAgo / 1440;
                return daysAgo + "d";
            }
        } catch (Exception e) {
            return "Unknown time";
        }
    }



}
