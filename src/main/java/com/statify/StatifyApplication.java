package com.statify;

import com.statify.telegramBot.StatifyBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

@SpringBootApplication
public class StatifyApplication implements CommandLineRunner {

    private final StatifyBot statifyBot;

    @Autowired
    public StatifyApplication(StatifyBot statifyBot) {
        this.statifyBot = statifyBot;
    }

    public static void main(String[] args) {
        SpringApplication.run(StatifyApplication.class, args);
    }

    @Override
    public void run(String... args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(statifyBot);
    }
}
