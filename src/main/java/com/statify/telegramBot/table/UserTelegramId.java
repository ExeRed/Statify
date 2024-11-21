package com.statify.telegramBot.table;

import javax.persistence.*;

@Entity
@Table(name = "telegram_ids")
public class UserTelegramId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String telegramUserId;
    private String username;  // Добавляем поле для имени пользователя

    public UserTelegramId() {}

    public UserTelegramId(String telegramUserId, String username) {
        this.telegramUserId = telegramUserId;
        this.username = username;
    }

    // Геттеры и сеттеры
    public String getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(String telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
